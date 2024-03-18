package org.example.jobsearch.service;

import org.example.jobsearch.dto.UserDto;
import org.example.jobsearch.exceptions.UserAlreadyRegisteredException;
import org.example.jobsearch.exceptions.UserHaveTooLowAge;
import org.example.jobsearch.exceptions.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    List<UserDto> getUsersByName(String name) throws UserNotFoundException;

    List<UserDto> getUsers();

    UserDto getUserByPhone(String phone) throws UserNotFoundException;

    UserDto getUserByEmail(String email) throws UserNotFoundException;

    String userIsExists(String email);

    void createUser(UserDto userDto) throws UserAlreadyRegisteredException, UserHaveTooLowAge;
}
