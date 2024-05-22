package org.example.jobsearch.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.jobsearch.models.User;
import org.example.jobsearch.repositories.UserRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class ApplicationLocaleResolver extends SessionLocaleResolver {
    private final UserRepository userRepository;

    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        String username = securityContext.getAuthentication().getName();
        Optional<User> user = userRepository.getByEmail(username);
        if (user.isEmpty()) {
            return getDefaultLocale();
        }
        try {
            String localeOption = userRepository.getLocaleByEmail(username);
            return Locale.forLanguageTag(localeOption);
        } catch (NullPointerException e){
            return getDefaultLocale();
        }
    }

    @Override
    public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
        super.setLocale(request, response, locale);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        String username = securityContext.getAuthentication().getName();
        if (Objects.equals(username, "anonymousUser")) {
            return;
        }
        userRepository.saveLocaleByEmail(username, locale.toString());
    }
}
