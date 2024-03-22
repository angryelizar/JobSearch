package org.example.jobsearch.exceptions.handler;

import lombok.RequiredArgsConstructor;
import org.example.jobsearch.exceptions.ErrorResponseBody;
import org.example.jobsearch.exceptions.ResumeException;
import org.example.jobsearch.exceptions.UserAlreadyRegisteredException;
import org.example.jobsearch.exceptions.UserHaveTooLowAgeException;
import org.example.jobsearch.service.ErrorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final ErrorService errorService;

    @ExceptionHandler(UserAlreadyRegisteredException.class)
    public ResponseEntity<ErrorResponseBody> userAlreadyRegistered(UserAlreadyRegisteredException exception) {
        return new ResponseEntity<>(errorService.makeResponse(exception), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserHaveTooLowAgeException.class)
    public ResponseEntity<ErrorResponseBody> userHaveTooLowAge(UserHaveTooLowAgeException exception) {
        return new ResponseEntity<>(errorService.makeResponse(exception), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResumeException.class)
    public ResponseEntity<ErrorResponseBody> resumeException(ResumeException exception) {
        return new ResponseEntity<>(errorService.makeResponse(exception), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseBody> validationHandler(MethodArgumentNotValidException exception) {
        return new ResponseEntity<>(errorService.makeResponse(exception.getBindingResult()), HttpStatus.BAD_REQUEST);
    }
}
