package org.example.jobsearch.util;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.jobsearch.models.User;
import org.example.jobsearch.service.UserService;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticatedUserProvider {
    private final UserService userService;

    @SneakyThrows
    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken){
            return null;
        }
        if (authentication != null) {
            return userService.getFullUserByEmail(authentication.getName());
        }
        return null;
    }

    public boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return !(authentication instanceof AnonymousAuthenticationToken);
    }
}
