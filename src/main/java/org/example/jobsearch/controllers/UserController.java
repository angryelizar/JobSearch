package org.example.jobsearch.controllers;

import lombok.RequiredArgsConstructor;
import org.example.jobsearch.dto.UserDto;
import org.example.jobsearch.exceptions.UserNotFoundException;
import org.example.jobsearch.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;


    @GetMapping("users")
    public ResponseEntity<List<UserDto>> getUsers(){
        return ResponseEntity.ok().body(userService.getUsers());
    }

    @GetMapping("users_by_name/{name}")
    public ResponseEntity<?> getUserByName(@PathVariable String name){
        try {
            List<UserDto> users = userService.getUsersByName(name);
            return ResponseEntity.ok().body(users);
        } catch (UserNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("users_by_phone/{phone}")
    public ResponseEntity<?> getUserByPhone(@PathVariable String phone){
        try {
            UserDto user = userService.getUserByPhone(phone);
            return ResponseEntity.ok().body(user);
        } catch (UserNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("users_by_email/{email}")
    public ResponseEntity<?> getUserByEmail(@PathVariable String email){
        try {
            UserDto user = userService.getUserByEmail(email);
            return ResponseEntity.ok().body(user);
        } catch (UserNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
