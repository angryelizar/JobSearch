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
    public static final String EMPLOYER = "EMPLOYER";
    public static final String APPLICANT = "APPLICANT";
    public static final String ADMIN = "ADMIN";

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
        http
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(Customizer.withDefaults())
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.GET, "/vacancies/inactive").hasAnyAuthority(ADMIN, EMPLOYER)
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**").permitAll()
                        .requestMatchers("/users/employers").permitAll()
                        .requestMatchers(HttpMethod.GET, "/users/{id}/avatar").permitAll()
                        .requestMatchers(HttpMethod.GET, "/vacancies").permitAll()
                        .requestMatchers(HttpMethod.GET, "/vacancies/category/{id}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/vacancies/{id}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/vacancies/active").permitAll()
                        .requestMatchers(HttpMethod.GET, "/vacancies/search/**").permitAll()
                        .requestMatchers("/profile").fullyAuthenticated()
                        .requestMatchers(HttpMethod.POST, "/users").anonymous()
                        .requestMatchers("/users/applicants").hasAuthority(EMPLOYER)
                        .requestMatchers("/users/name/{name}").hasAuthority(EMPLOYER)
                        .requestMatchers(HttpMethod.POST, "/users/avatar").hasAnyAuthority(EMPLOYER, ADMIN, APPLICANT)
                        .requestMatchers("/users/email/{email}").hasAuthority(ADMIN)
                        .requestMatchers("/users/phone/{phone}").hasAuthority(ADMIN)
                        .requestMatchers("/users/exists/{email}").hasAuthority(ADMIN)

                        .requestMatchers(HttpMethod.GET, "/resumes").hasAnyAuthority(EMPLOYER, ADMIN)
                        .requestMatchers(HttpMethod.POST, "/resumes").hasAnyAuthority(APPLICANT, ADMIN)
                        .requestMatchers(HttpMethod.POST, "/resumes/{id}").hasAnyAuthority(APPLICANT, ADMIN)
                        .requestMatchers(HttpMethod.DELETE, "/resumes/{id}").hasAnyAuthority(APPLICANT, ADMIN)
                        .requestMatchers(HttpMethod.GET, "/resumes/active").hasAnyAuthority(EMPLOYER, ADMIN)
                        .requestMatchers(HttpMethod.GET, "/resumes/inactive").hasAuthority(ADMIN)
                        .requestMatchers(HttpMethod.GET, "/resumes/search/applicants").hasAnyAuthority(EMPLOYER, ADMIN)
                        .requestMatchers(HttpMethod.GET, "/resumes/search").hasAnyAuthority(EMPLOYER, ADMIN)
                        .requestMatchers(HttpMethod.GET, "/resumes/category/{id}").hasAnyAuthority(EMPLOYER, ADMIN)
                        .requestMatchers(HttpMethod.GET, "/resumes/user/{id}").hasAnyAuthority(EMPLOYER, ADMIN)

                        .requestMatchers(HttpMethod.POST, "/vacancies").hasAnyAuthority(ADMIN, EMPLOYER)
                        .requestMatchers(HttpMethod.POST, "/vacancies/responded-applicant").hasAnyAuthority(ADMIN, APPLICANT)
                        .requestMatchers(HttpMethod.POST, "/vacancies/{id}").hasAnyAuthority(ADMIN, EMPLOYER)
                        .requestMatchers(HttpMethod.DELETE, "/vacancies/{id}").hasAnyAuthority(ADMIN, EMPLOYER)
                        .requestMatchers(HttpMethod.GET, "/vacancies/applicant/{id}").hasAnyAuthority(ADMIN, EMPLOYER)
                        .requestMatchers("/vacancies/search/employer/**").hasAnyAuthority(ADMIN, EMPLOYER)
                        .requestMatchers(HttpMethod.GET, "/vacancies/{id}/responded-applicants").hasAnyAuthority(ADMIN, EMPLOYER)
                        .requestMatchers(HttpMethod.GET, "/vacancies/{id}/users").hasAnyAuthority(ADMIN, EMPLOYER)
                );
        return http.build();
    }
}
