package org.example.jobsearch.exceptions.handler;

import lombok.RequiredArgsConstructor;
import org.example.jobsearch.exceptions.*;
import org.example.jobsearch.service.ErrorService;
import org.example.jobsearch.util.AuthenticatedUserProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final ErrorService errorService;
    private final AuthenticatedUserProvider authenticatedUserProvider;

    @ExceptionHandler(UserException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponseBody> userAlreadyRegistered(UserException exception) {
        return new ResponseEntity<>(errorService.makeResponse(exception), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResumeException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponseBody> resumeException(ResumeException exception) {
        return new ResponseEntity<>(errorService.makeResponse(exception), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(VacancyException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponseBody> vacancyException(VacancyException exception) {
        return new ResponseEntity<>(errorService.makeResponse(exception), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponseBody> validationHandler(MethodArgumentNotValidException exception) {
        return new ResponseEntity<>(errorService.makeResponse(exception.getBindingResult()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmptyMessageException.class)
    public String emptyMessageException(EmptyMessageException exception, Model model) {
        model.addAttribute("pageTitle", "Ошибка");
        model.addAttribute("exceptionText", exception.getMessage());
        model.addAttribute("redirectLink", "/messages");
        model.addAttribute("redirectLinkTitle", "Сообщения");
        model.addAttribute("isAuthenticated", authenticatedUserProvider.isAuthenticated());
        model.addAttribute("isEmployer", authenticatedUserProvider.isEmployer());
        return "error/error";
    }

    @ExceptionHandler(AvatarException.class)
    public String avatarException(AvatarException exception, Model model) {
        model.addAttribute("pageTitle", "Ошибка");
        model.addAttribute("exceptionText", exception.getMessage());
        model.addAttribute("redirectLink", "/profile");
        model.addAttribute("redirectLinkTitle", "Профиль");
        model.addAttribute("isAuthenticated", authenticatedUserProvider.isAuthenticated());
        model.addAttribute("isEmployer", authenticatedUserProvider.isEmployer());
        return "error/error";
    }

    @ExceptionHandler(ServiceException.class)
    public String serviceException(ServiceException exception, Model model) {
        model.addAttribute("pageTitle", "Ошибка");
        model.addAttribute("exceptionText", exception.getMessage());
        model.addAttribute("redirectLink", "/profile");
        model.addAttribute("redirectLinkTitle", "Профиль");
        model.addAttribute("isAuthenticated", authenticatedUserProvider.isAuthenticated());
        model.addAttribute("isEmployer", authenticatedUserProvider.isEmployer());
        return "error/error";
    }

    @ExceptionHandler(UserAlreadyRegisteredException.class)
    public String userAlreadyRegistered(UserAlreadyRegisteredException exception, Model model) {
        model.addAttribute("pageTitle", "Ошибка");
        model.addAttribute("exceptionText", exception.getMessage());
        model.addAttribute("redirectLink", "/registration");
        model.addAttribute("redirectLinkTitle", "Регистрация");
        model.addAttribute("isAuthenticated", authenticatedUserProvider.isAuthenticated());
        model.addAttribute("isEmployer", authenticatedUserProvider.isEmployer());
        return "error/error";
    }

    @ExceptionHandler(UserHaveTooLowAgeException.class)
    public String userHaveTooLowAge(UserHaveTooLowAgeException exception, Model model) {
        model.addAttribute("pageTitle", "Ошибка");
        model.addAttribute("exceptionText", exception.getMessage());
        model.addAttribute("redirectLink", "/registration");
        model.addAttribute("redirectLinkTitle", "Регистрация");
        model.addAttribute("isAuthenticated", authenticatedUserProvider.isAuthenticated());
        model.addAttribute("isEmployer", authenticatedUserProvider.isEmployer());
        return "error/error";
    }

}
