package org.example.jobsearch.service.impl;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
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
import org.example.jobsearch.service.EmailService;
import org.example.jobsearch.service.UserService;
import org.example.jobsearch.util.ToPageUtil;
import org.example.jobsearch.util.URLUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.util.*;

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
    private static final String APPLICANT = "Соискатель";
    private final EmailService emailService;

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
        User user = userRepository.getByEmail(email).orElseThrow(() -> new UserNotFoundException("С такой почтой пользователей не найдено - " + email));
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
        return userRepository.getByEmail(email).orElseThrow(() -> new UserNotFoundException("С такой почтой пользователей не найдено - " + email));
    }

    @Override
    public String userIsExists(String email) {
        boolean result = userRepository.existsByEmail(email);
        return result ? "Пользователь существует" : "Пользователя нет в системе";
    }

    @Override
    @SneakyThrows
    public void createUser(UserDto userDto, HttpServletRequest request) {
        if (userRepository.existsByEmail(userDto.getEmail()) || userRepository.existsByPhoneNumber(userDto.getPhoneNumber())) {
            throw new UserAlreadyRegisteredException("exception.registration.alreadyRegistered");
        }
        User user = new User();
        user.setName(userDto.getName());
        user.setSurname(userDto.getSurname());
        user.setEmail(userDto.getEmail());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setPassword(encoder.encode(userDto.getPassword()));
        user.setAvatar("default_avatar.jpeg");
        user.setAccountType(userDto.getAccountType());
        if (user.getAccountType().equals("Соискатель")){
            user.setAge(userDto.getAge());
        }
        user.setEnabled(true);
        user.setAuthority(authorityService.getAccountAuthorityByTypeString(user.getAccountType()));
        userRepository.save(user);
        request.login(userDto.getEmail(), userDto.getPassword());
    }

    @Override
    @SneakyThrows
    @Transactional
    public void update(UserDto userDto) {
        var user = userRepository.getByEmail(userDto.getEmail());
        if (user.isEmpty()) {
            log.error("Запрошен несуществующий пользователь с e-mail " + userDto.getEmail());
            throw new ServiceException("exception.profile.noUser");
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
            throw new AvatarException("exception.avatar.empty");
        }
        if (file.getSize() > 1000000) {
            throw new AvatarException("exception.avatar.tooMuchSize");
        }
        if (!file.getOriginalFilename().endsWith(".jpeg") && !file.getOriginalFilename().endsWith(".jpg") && !file.getOriginalFilename().endsWith(".png")) {
            throw new AvatarException("exception.avatar.allowedFiles");
        }
        avatarImageService.upload(user.get(), file);
    }

    @Override
    @SneakyThrows
    public UserDto getUserById(Long id) {
        Optional<User> maybeUser = userRepository.findById(id);
        if (maybeUser.isEmpty()) {
            log.error("Был запрошен несуществующий пользователь с ID " + id);
            throw new UserException("exception.profile.noUser");
        }
        User user = maybeUser.get();
        return UserDto.builder()
                .name(user.getName())
                .surname(user.getSurname())
                .age(user.getAge())
                .build();
    }

    @Override
    public boolean isApplicant(String email) {
        User user = getFullUserByEmail(email);
        return user != null && user.getAccountType().equals(APPLICANT);
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

    @Override
    public Map<String, String> getAccountTypes() {
        Map<String, String> accountTypes = new HashMap<>();
        accountTypes.put("Работодатель", "Работодатель");
        accountTypes.put(APPLICANT, APPLICANT);
        return accountTypes;
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

    @SneakyThrows
    private void updateResetPassword(String token, String email) {
        User user = userRepository.getByEmail(email).orElseThrow(() -> new ServiceException("exception.profile.noUser"));
        user.setResetPasswordToken(token);
        userRepository.saveAndFlush(user);
    }

    @Override
    @SneakyThrows
    public User getByResetPasswordToken(String token) {
        return userRepository.getUserByResetPasswordToken(token).orElseThrow(() -> new ServiceException("exception.profile.noUser"));
    }

    @Override
    public void updatePassword(User user, String password) {
        if (password.isEmpty() || password.isBlank()){
            throw new IllegalArgumentException("exception.resetPassword.empty");
        }
        if (password.length() < 4 || password.length() > 24){
            throw new IllegalArgumentException("exception.resetPassword.length");
        }
        if (!password.matches("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).+$")){
            throw new IllegalArgumentException("exception.resetPassword.symbols");
        }
        String encodedPassword = encoder.encode(password);
        user.setPassword(encodedPassword);
        user.setResetPasswordToken(null);
        userRepository.saveAndFlush(user);
    }

    @Override
    public void makeResetPasswordLink(HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        String email = request.getParameter("email");
        String token = UUID.randomUUID().toString();
        updateResetPassword(token, email);
        String resetPasswordLink = URLUtil.getSiteURL(request) + "/reset_password?token=" + token;
        emailService.sendEmail(email, resetPasswordLink, userRepository.getByEmail(email).get().getName());
    }
}
