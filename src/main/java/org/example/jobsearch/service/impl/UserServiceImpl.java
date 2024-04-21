package org.example.jobsearch.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.example.jobsearch.config.AppConfig;
import org.example.jobsearch.dao.ResumeDao;
import org.example.jobsearch.dao.UserDao;
import org.example.jobsearch.dao.VacancyDao;
import org.example.jobsearch.dto.ApplicantInfoDto;
import org.example.jobsearch.dto.EmployerInfoDto;
import org.example.jobsearch.dto.UserDto;
import org.example.jobsearch.exceptions.*;
import org.example.jobsearch.models.Resume;
import org.example.jobsearch.models.User;
import org.example.jobsearch.models.Vacancy;
import org.example.jobsearch.service.AuthorityService;
import org.example.jobsearch.service.AvatarImageService;
import org.example.jobsearch.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    private final VacancyDao vacancyDao;
    private final ResumeDao resumeDao;
    private final AuthorityService authorityService;
    private final AvatarImageService avatarImageService;
    private final PasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public List<UserDto> getUsersByName(String name) throws UserNotFoundException {
        List<User> users = userDao.getUsersByName(name);
        if (users == null || users.isEmpty()) {
            throw new UserNotFoundException("Пользователи с таким именем не найдены");
        }
        List<UserDto> userDtos = new ArrayList<>();
        users.forEach(e -> userDtos.add(UserDto.builder()
                .name(e.getName())
                .surname(e.getSurname())
                .accountType(e.getAccountType())
                .build()));
        return userDtos;
    }

    @Override
    public List<UserDto> getUsers() {
        List<User> users = userDao.getUsers();
        List<UserDto> userDtos = new ArrayList<>();
        users.forEach(e -> userDtos.add(UserDto.builder()
                .name(e.getName())
                .surname(e.getSurname())
                .accountType(e.getAccountType())
                .build()));
        return userDtos;
    }

    @Override
    public List<UserDto> getApplicantsUsers() {
        List<User> users = userDao.getApplicantsUsers();
        List<UserDto> userDtos = new ArrayList<>();
        users.forEach(e -> userDtos.add(UserDto.builder()
                .name(e.getName())
                .surname(e.getSurname())
                .accountType(e.getAccountType())
                .build()));
        return userDtos;
    }

    @Override
    public List<UserDto> getEmployersUsers() {
        List<User> users = userDao.getEmployersUsers();
        List<UserDto> userDtos = new ArrayList<>();
        users.forEach(e -> userDtos.add(UserDto.builder()
                        .id(e.getId())
                .name(e.getName())
                .surname(e.getSurname())
                .accountType(e.getAccountType())
                .build()));
        return userDtos;
    }

    @Override
    public UserDto getUserByPhone(String phone) throws UserNotFoundException {
        User user = userDao.getUserByPhone(phone).orElseThrow(() -> new UserNotFoundException("С таким номером пользователей не найдено - " + phone));
        return UserDto.builder()
                .name(user.getName())
                .surname(user.getSurname())
                .accountType(user.getAccountType())
                .build();
    }

    @Override
    public UserDto getUserByEmail(String email) throws UserNotFoundException {
        User user = userDao.getUserByEmail(email).orElseThrow(() -> new UserNotFoundException("С такой почтой пользователей не найдено - " + email));
        return UserDto.builder()
                .name(user.getName())
                .surname(user.getSurname())
                .age(user.getAge())
                .accountType(user.getAccountType())
                .build();
    }

    @Override
    @SneakyThrows
    public User getFullUserByEmail(String email) {
        return userDao.getUserByEmail(email).orElseThrow(() -> new UserNotFoundException("С такой почтой пользователей не найдено - " + email));
    }

    @Override
    public String userIsExists(String email) {
        boolean result = userDao.emailIsExists(email);
        return result ? "Пользователь существует" : "Пользователя нет в системе";
    }

    @Override
    @SneakyThrows
    public void createUser(UserDto userDto) {
        if (userDao.emailIsExists(userDto.getEmail()) || userDao.phoneIsExists(userDto.getPhoneNumber())) {
            throw new UserAlreadyRegisteredException("Пользователь уже зарегистрирован");
        }
        if (userDto.getAge() < 18) {
            throw new UserHaveTooLowAgeException("Пользователь слишком молод!");
        }
        User user = new User();
        user.setName(userDto.getName());
        user.setSurname(userDto.getSurname());
        user.setAge(userDto.getAge());
        user.setEmail(userDto.getEmail());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setPassword(encoder.encode(userDto.getPassword()));
        user.setAvatar(userDto.getAvatar());
        user.setAccountType(userDto.getAccountType());
        Long userId = userDao.createUser(user);
        authorityService.add(userId, getAccountTypeIdByTypeString(user.getAccountType()));
    }

    @Override
    @SneakyThrows
    @Transactional
    public void update(UserDto userDto) {
        var user = userDao.getUserByEmail(userDto.getEmail());
        if (user.isEmpty()) {
            log.error("Запрошен несуществующий пользователь с e-mail " + userDto.getEmail());
            throw new UserNotFoundException("Такого пользователя нет!");
        }
        Long userId = user.get().getId();
        userDao.changeNameOfUser(userDto.getName(), userId);
        userDao.changeSurnameOfUser(userDto.getSurname(), userId);
        userDao.changeAgeOfUser(userDto.getAge(), userId);
        userDao.changeEmailOfUser(userDto.getEmail(), userId);
        userDao.changePasswordOfUser(encoder.encode(userDto.getPassword()), userId);
        userDao.changePhoneOfUser(userDto.getPhoneNumber(), userId);
        if (!userDto.getAvatarFile().isEmpty()) {
            avatarImageService.upload(user.get(), userDto.getAvatarFile());
        }
    }

    @Override
    @SneakyThrows
    public UserDto getUserById(Long id) {
        Optional<User> maybeUser = userDao.getUserById(id);
        if (maybeUser.isEmpty()) {
            log.error("Был запрошен несуществующий пользователь с ID " + id);
            throw new UserException("Такого пользователя нет!");
        }
        User user = maybeUser.get();
        return UserDto.builder()
                .name(user.getName())
                .surname(user.getSurname())
                .age(user.getAge())
                .build();
    }

    public Long getAccountTypeIdByTypeString(String type) {
        return authorityService.getAccountAuthorityByTypeString(type);
    }

    @Override
    public boolean isApplicant(String email) {
        User user = getFullUserByEmail(email);
        return user != null && user.getAccountType().equals("Соискатель");
    }

    @Override
    public boolean isApplicantByAuth(Authentication auth) {
        try {
            return isApplicant(auth.getName());
        } catch (NullPointerException e) {
            return false;
        }
    }

    @Override
    @SneakyThrows
    public EmployerInfoDto getEmployerInfoByVacancyId(Long id) {
        Optional<Vacancy> maybeVacancy = vacancyDao.getVacancyById(id);
        if (maybeVacancy.isEmpty()) {
            throw new VacancyException("Вакансии с ID " + id + " не существует!");
        }
        Vacancy vacancy = maybeVacancy.get();
        Optional<User> maybeUser = userDao.getUserById(vacancy.getAuthorId());
        if (maybeUser.isEmpty()) {
            throw new UserNotFoundException("Пользователя с ID " + maybeVacancy.get().getAuthorId() + " не существует!");
        }
        User user = maybeUser.get();
        Integer count = vacancyDao.getCountByAuthorId(user.getId());
        return EmployerInfoDto.builder()
                .id(user.getId())
                .name(user.getName())
                .avatar(user.getAvatar())
                .activeVacancies(count)
                .build();
    }

    @Override
    @SneakyThrows
    public ApplicantInfoDto getApplicantInfoByResumeId(Long id) {
        Optional<Resume> maybeResume = resumeDao.getResumeById(id);
        if (maybeResume.isEmpty()) {
            throw new ResumeException("Резюме с ID " + id + "не существует");
        }
        Resume resume = maybeResume.get();
        Optional<User> maybeUser = userDao.getUserById(resume.getApplicantId());
        if (maybeUser.isEmpty()) {
            throw new UserNotFoundException("Юзера с ID " + id + " не существует!");
        }
        User user = maybeUser.get();
        Integer count = resumeDao.getCountByAuthorId(user.getId());
        return ApplicantInfoDto.builder()
                .id(user.getId())
                .name(user.getName())
                .surname(user.getSurname())
                .avatar(user.getAvatar())
                .activeResumes(count)
                .build();
    }
}
