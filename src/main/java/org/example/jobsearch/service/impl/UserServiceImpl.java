package org.example.jobsearch.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.example.jobsearch.dto.ApplicantInfoDto;
import org.example.jobsearch.dto.EmployerInfoDto;
import org.example.jobsearch.dto.UserDto;
import org.example.jobsearch.exceptions.*;
import org.example.jobsearch.models.Resume;
import org.example.jobsearch.models.User;
import org.example.jobsearch.models.Vacancy;
import org.example.jobsearch.repositories.ResumeRepository;
import org.example.jobsearch.repositories.UserRepository;
import org.example.jobsearch.repositories.VacancyRepository;
import org.example.jobsearch.service.AuthorityService;
import org.example.jobsearch.service.AvatarImageService;
import org.example.jobsearch.service.UserService;
import org.example.jobsearch.util.ToPageUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final AuthorityService authorityService;
    private final AvatarImageService avatarImageService;
    private final PasswordEncoder encoder = new BCryptPasswordEncoder();
    private final UserRepository userRepository;
    private final VacancyRepository vacancyRepository;
    private final ResumeRepository resumeRepository;

    @Override
    public List<UserDto> getUsersByName(String name) throws UserNotFoundException {
        List<User> users = userRepository.findByName(name);
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
        List<User> users = userRepository.findAll();
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
        List<User> users = userRepository.getApplicantsUsers();
        List<UserDto> userDtos = new ArrayList<>();
        users.forEach(e -> userDtos.add(UserDto.builder()
                .name(e.getName())
                .surname(e.getSurname())
                .accountType(e.getAccountType())
                .build()));
        return userDtos;
    }

    @Override
    public Page<UserDto> getEmployersUsers(Pageable pageable) {
        return ToPageUtil.toPageEmployers(makeUserDtoList(userRepository.getEmployersUsers()), pageable);
    }

    @Override
    public List<UserDto> getEmployersUsers() {
        List<User> users = userRepository.getEmployersUsers();
        return makeUserDtoList(users);
    }

    @Override
    public UserDto getUserByPhone(String phone) throws UserNotFoundException {
        User user = userRepository.getUserByPhoneNumber(phone).orElseThrow(() -> new UserNotFoundException("С таким номером пользователей не найдено - " + phone));
        return UserDto.builder()
                .name(user.getName())
                .surname(user.getSurname())
                .accountType(user.getAccountType())
                .build();
    }

    @Override
    public UserDto getUserByEmail(String email) throws UserNotFoundException {
        User user = userRepository.getUserByEmail(email).orElseThrow(() -> new UserNotFoundException("С такой почтой пользователей не найдено - " + email));
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
        return userRepository.getUserByEmail(email).orElseThrow(() -> new UserNotFoundException("С такой почтой пользователей не найдено - " + email));
    }

    @Override
    public String userIsExists(String email) {
        boolean result = userRepository.existsByEmail(email);
        return result ? "Пользователь существует" : "Пользователя нет в системе";
    }

    @Override
    @SneakyThrows
    public void createUser(UserDto userDto) {
        if (userRepository.existsByEmail(userDto.getEmail()) || userRepository.existsByPhoneNumber(userDto.getPhoneNumber())) {
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
        user.setAvatar("default_avatar.jpeg");
        user.setAccountType(userDto.getAccountType());
        user.setEnabled(true);
        Long userId = userRepository.save(user).getId();
        authorityService.add(userId, getAccountTypeIdByTypeString(user.getAccountType()));
    }

    @Override
    @SneakyThrows
    @Transactional
    public void update(UserDto userDto) {
        var user = userRepository.getUserByEmail(userDto.getEmail());
        if (user.isEmpty()) {
            log.error("Запрошен несуществующий пользователь с e-mail " + userDto.getEmail());
            throw new ServiceException("Такого пользователя нет!");
        }
        User updateUser = user.get();
        updateUser.setName(userDto.getName());
        updateUser.setSurname(userDto.getSurname());
        updateUser.setAge(userDto.getAge());
        updateUser.setEmail(userDto.getEmail());
        updateUser.setPassword(encoder.encode(userDto.getPassword()));
        updateUser.setPhoneNumber(userDto.getPhoneNumber());
        userRepository.save(updateUser);
        MultipartFile file = userDto.getAvatarFile();
        if (file.isEmpty()) {
            throw new AvatarException("Нельзя отправлять пустой файл!");
        }
        if (file.getSize() > 1000000) {
            throw new AvatarException("Нельзя отправлять файл больше 1000 килобайт!");
        }
        if (!file.getOriginalFilename().endsWith(".jpeg") && !file.getOriginalFilename().endsWith(".jpg") && !file.getOriginalFilename().endsWith(".png")) {
            throw new AvatarException("Разрешены к загрузке только картинки .jpeg, .jpg и .png!");
        }
        avatarImageService.upload(user.get(), file);
    }

    @Override
    @SneakyThrows
    public UserDto getUserById(Long id) {
        Optional<User> maybeUser = userRepository.findById(id);
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
        Optional<Vacancy> maybeVacancy = vacancyRepository.findById(id);
        if (maybeVacancy.isEmpty()) {
            throw new VacancyException("Вакансии с ID " + id + " не существует!");
        }
        Vacancy vacancy = maybeVacancy.get();
        Optional<User> maybeUser = userRepository.findById(vacancy.getAuthor().getId());
        if (maybeUser.isEmpty()) {
            throw new UserNotFoundException("Пользователя с ID " + maybeVacancy.get().getAuthor().getId() + " не существует!");
        }
        User user = maybeUser.get();
        Integer count = vacancyRepository.getCountVacanciesByAuthorId(user.getId());
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
        Optional<Resume> maybeResume = resumeRepository.findById(id);
        if (maybeResume.isEmpty()) {
            throw new ResumeException("Резюме с ID " + id + "не существует");
        }
        Resume resume = maybeResume.get();
        Optional<User> maybeUser = userRepository.findById(resume.getApplicant().getId());
        if (maybeUser.isEmpty()) {
            throw new UserNotFoundException("Юзера с ID " + id + " не существует!");
        }
        User user = maybeUser.get();
        Integer count = resumeRepository.countByApplicantId(user.getId());
        return ApplicantInfoDto.builder()
                .id(user.getId())
                .name(user.getName())
                .surname(user.getSurname())
                .avatar(user.getAvatar())
                .activeResumes(count)
                .build();
    }

    private List<UserDto> makeUserDtoList(List<User> userList) {
        List<UserDto> userDtos = new ArrayList<>();
        userList.forEach(e -> userDtos.add(UserDto.builder()
                .id(e.getId())
                .name(e.getName())
                .surname(e.getSurname())
                .accountType(e.getAccountType())
                .build()));
        return userDtos;
    }
}
