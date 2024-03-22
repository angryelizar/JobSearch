package org.example.jobsearch.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.jobsearch.exceptions.ErrorResponseBody;
import org.example.jobsearch.service.ErrorService;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ErrorServiceImpl implements ErrorService {

    @Override
    public ErrorResponseBody makeResponse(Exception exception){
        String message = exception.getMessage();
        return ErrorResponseBody.builder()
                .title(message)
                .reasons(List.of(message))
                .build();
    }

    @Override
    public ErrorResponseBody makeResponse(BindingResult exception) {
        List<String> errors = new ArrayList<>();

        for (ObjectError error : exception.getAllErrors()) {
            errors.add(error.getDefaultMessage());
        }
        return ErrorResponseBody.builder()
                .title("Ошибка валидации")
                .reasons(errors)
                .build();
    }
}
