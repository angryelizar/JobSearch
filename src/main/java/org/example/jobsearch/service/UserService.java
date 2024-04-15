package org.example.jobsearch.service;

import org.example.jobsearch.dto.UserDto;
import org.example.jobsearch.exceptions.UserAlreadyRegisteredException;
import org.example.jobsearch.exceptions.UserHaveTooLowAgeException;
import org.example.jobsearch.exceptions.UserNotFoundException;
import org.example.jobsearch.models.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    List<UserDto> getUsersByName(String name) throws UserNotFoundException;

    List<UserDto> getUsers();

    List<UserDto> getApplicantsUsers();

    List<UserDto> getEmployersUsers();

    UserDto getUserByPhone(String phone) throws UserNotFoundException;

    UserDto getUserByEmail(String email) throws UserNotFoundException;

    User getFullUserByEmail(String email) throws UserNotFoundException;

    String userIsExists(String email);

    void createUser(UserDto userDto);

    void update(UserDto userDto);

    UserDto getUserById(Long id);
    boolean isApplicant (String email);

    boolean isApplicantByAuth(Authentication auth);
}
