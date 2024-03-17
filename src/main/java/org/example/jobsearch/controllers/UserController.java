package org.example.jobsearch.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.jobsearch.dto.UserDto;
import org.example.jobsearch.exceptions.UserAlreadyRegisteredException;
import org.example.jobsearch.exceptions.UserHaveTooLowAge;
import org.example.jobsearch.exceptions.UserNotFoundException;
import org.example.jobsearch.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @GetMapping()
    public ResponseEntity<List<UserDto>> getUsers(){
        return ResponseEntity.ok().body(userService.getUsers());
    }

    @GetMapping("name/{name}")
    public ResponseEntity<?> getUserByName(@PathVariable String name){
        try {
            List<UserDto> users = userService.getUsersByName(name);
            return ResponseEntity.ok().body(users);
        } catch (UserNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("phone/{phone}")
    public ResponseEntity<?> getUserByPhone(@PathVariable String phone){
        try {
            UserDto user = userService.getUserByPhone(phone);
            return ResponseEntity.ok().body(user);
        } catch (UserNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("email/{email}")
    public ResponseEntity<?> getUserByEmail(@PathVariable String email){
        try {
            UserDto user = userService.getUserByEmail(email);
            return ResponseEntity.ok().body(user);
        } catch (UserNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("exists/{email}")
    public ResponseEntity<String> userIsExists(@PathVariable String email){
        return ResponseEntity.ok(userService.userIsExists(email));
    }

    @PostMapping()
    public HttpStatus createUser(@RequestBody UserDto userDto){
        try {
            userService.createUser(userDto);
            return HttpStatus.CREATED;
        } catch (UserAlreadyRegisteredException | UserHaveTooLowAge e){
            log.info("Пользователь либо существует, либо он слишком молод!");
            return HttpStatus.NO_CONTENT;
        }

    }
}
