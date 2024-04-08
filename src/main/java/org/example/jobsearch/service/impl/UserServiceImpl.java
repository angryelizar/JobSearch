package org.example.jobsearch.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.example.jobsearch.config.AppConfig;
import org.example.jobsearch.dao.UserDao;
import org.example.jobsearch.dto.UserDto;
import org.example.jobsearch.exceptions.UserAlreadyRegisteredException;
import org.example.jobsearch.exceptions.UserHaveTooLowAgeException;
import org.example.jobsearch.exceptions.UserNotFoundException;
import org.example.jobsearch.models.User;
import org.example.jobsearch.service.AuthorityService;
import org.example.jobsearch.service.AvatarImageService;
import org.example.jobsearch.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
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
        if (!userDto.getAvatarFile().isEmpty()){
            avatarImageService.upload(user.get(), userDto.getAvatarFile());
        }
    }

    public Long getAccountTypeIdByTypeString(String type) {
        return authorityService.getAccountAuthorityByTypeString(type);
    }
}
