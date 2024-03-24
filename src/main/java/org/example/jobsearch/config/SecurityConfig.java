package org.example.jobsearch.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final DataSource dataSource;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        String authoritiesQuery = """
                select u.EMAIL, a.AUTHORITY from ROLES ua, AUTHORITIES a, USERS u
                where ua.AUTHORITY_ID = a.id
                and ua.USER_ID = u.ID and u.EMAIL = ?
                """;
        String userQuery = """
                select EMAIL, PASSWORD, ENABLED from USERS
                WHERE email = ?;
                """;
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery(userQuery)
                .authoritiesByUsernameQuery(authoritiesQuery);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        String EMPLOYER = "EMPLOYER";
        String APPLICANT = "APPLICANT";
        String ADMIN = "ADMIN";
        http
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(Customizer.withDefaults())
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/users").permitAll()
                        .requestMatchers("/users/applicants").hasAuthority(EMPLOYER)
                        .requestMatchers("/users/employers").permitAll()
                        .requestMatchers("/users/name/{name}").hasAuthority(EMPLOYER)
                        .requestMatchers(HttpMethod.POST, "/users/avatar").hasAnyAuthority(EMPLOYER, ADMIN, APPLICANT)
                        .requestMatchers(HttpMethod.GET,"/users/{id}/avatar").permitAll()
                        .requestMatchers("/users/email/{email}").hasAuthority(ADMIN)
                        .requestMatchers("users/phone/{phone}").hasAuthority(ADMIN)
                        .requestMatchers("users/exists/{email}").hasAuthority(ADMIN)
                        .requestMatchers("/resumes").hasAuthority(EMPLOYER));
        return http.build();
    }
}
