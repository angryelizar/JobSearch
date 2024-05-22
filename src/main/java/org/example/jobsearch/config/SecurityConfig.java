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
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    public static final String EMPLOYER = "EMPLOYER";
    public static final String APPLICANT = "APPLICANT";
    public static final String ADMIN = "ADMIN";

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
                .httpBasic(Customizer.withDefaults())
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/profile")
                        .successHandler(new LoginSuccessHandler())
                        .failureUrl("/login-failed")
                        .permitAll())
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .permitAll())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("api/messages/**").fullyAuthenticated()
                        .requestMatchers("api/vacancies/resume").permitAll()
                        .requestMatchers("/login").anonymous()
                        .requestMatchers("/registration").anonymous()
                        .requestMatchers("/profile").fullyAuthenticated()
                        .requestMatchers("/messages").fullyAuthenticated()
                        .requestMatchers("/resumes").hasAnyAuthority(ADMIN, EMPLOYER)
                        .requestMatchers(HttpMethod.POST, "/messages/**").fullyAuthenticated()

                        .requestMatchers("/resumes/add").hasAuthority(APPLICANT)
                        .requestMatchers("resumes/edit").hasAuthority(APPLICANT)
                        .requestMatchers("/resumes/delete").hasAuthority(APPLICANT)
                        .requestMatchers("/applicant/responses").hasAuthority(APPLICANT)

                        .requestMatchers("/vacancies/add").hasAuthority(EMPLOYER)
                        .requestMatchers("/vacancies/edit").hasAuthority(EMPLOYER)
                        .requestMatchers("/vacancies/delete").hasAuthority(EMPLOYER)
                        .requestMatchers("/employer/responses/**").hasAuthority(EMPLOYER)

                        .requestMatchers(HttpMethod.GET, "api/vacancies/inactive").hasAnyAuthority(ADMIN, EMPLOYER)
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**").permitAll()
                        .requestMatchers("api/users/employers").permitAll()
                        .requestMatchers(HttpMethod.GET, "api/users/{id}/avatar").permitAll()
                        .requestMatchers(HttpMethod.GET, "api/vacancies").permitAll()
                        .requestMatchers(HttpMethod.GET, "api/vacancies/category/{id}").permitAll()
                        .requestMatchers(HttpMethod.GET, "api/vacancies/{id}").permitAll()
                        .requestMatchers(HttpMethod.GET, "api/vacancies/active").permitAll()
                        .requestMatchers(HttpMethod.GET, "api/vacancies/search/**").permitAll()
                        .requestMatchers("/profile").fullyAuthenticated()
                        .requestMatchers(HttpMethod.POST, "api/users").anonymous()
                        .requestMatchers("api/users/applicants").hasAuthority(EMPLOYER)
                        .requestMatchers("api/users/name/{name}").hasAuthority(EMPLOYER)
                        .requestMatchers(HttpMethod.POST, "api/users/avatar").hasAnyAuthority(EMPLOYER, ADMIN, APPLICANT)
                        .requestMatchers("api/users/email/{email}").hasAuthority(ADMIN)
                        .requestMatchers("api/users/phone/{phone}").hasAuthority(ADMIN)
                        .requestMatchers("api/users/exists/{email}").hasAuthority(ADMIN)

                        .requestMatchers(HttpMethod.GET, "api/resumes").hasAnyAuthority(EMPLOYER, ADMIN)
                        .requestMatchers(HttpMethod.POST, "api/resumes").hasAnyAuthority(APPLICANT, ADMIN)
                        .requestMatchers(HttpMethod.POST, "api/resumes/{id}").hasAnyAuthority(APPLICANT, ADMIN)
                        .requestMatchers(HttpMethod.DELETE, "api/resumes/{id}").hasAnyAuthority(APPLICANT, ADMIN)
                        .requestMatchers(HttpMethod.GET, "api/resumes/active").hasAnyAuthority(EMPLOYER, ADMIN)
                        .requestMatchers(HttpMethod.GET, "api/resumes/inactive").hasAuthority(ADMIN)
                        .requestMatchers(HttpMethod.GET, "api/resumes/search/applicants").hasAnyAuthority(EMPLOYER, ADMIN)
                        .requestMatchers(HttpMethod.GET, "api/resumes/search").hasAnyAuthority(EMPLOYER, ADMIN)
                        .requestMatchers(HttpMethod.GET, "api/resumes/category/{id}").hasAnyAuthority(EMPLOYER, ADMIN)
                        .requestMatchers(HttpMethod.GET, "api/resumes/user/{id}").hasAnyAuthority(EMPLOYER, ADMIN)

                        .requestMatchers(HttpMethod.POST, "api/vacancies").hasAnyAuthority(ADMIN, EMPLOYER)
                        .requestMatchers(HttpMethod.POST, "api/vacancies/responded-applicant").hasAnyAuthority(ADMIN, APPLICANT)
                        .requestMatchers(HttpMethod.POST, "api/vacancies/{id}").hasAnyAuthority(ADMIN, EMPLOYER)
                        .requestMatchers(HttpMethod.DELETE, "/vacancies/{id}").hasAnyAuthority(ADMIN, EMPLOYER)
                        .requestMatchers(HttpMethod.GET, "api/vacancies/applicant/{id}").hasAnyAuthority(ADMIN, EMPLOYER)
                        .requestMatchers("api/vacancies/search/employer/**").hasAnyAuthority(ADMIN, EMPLOYER)
                        .requestMatchers(HttpMethod.GET, "api/vacancies/{id}/responded-applicants").hasAnyAuthority(ADMIN, EMPLOYER)
                        .requestMatchers(HttpMethod.GET, "api/vacancies/{id}/users").hasAnyAuthority(ADMIN, EMPLOYER)
                        .anyRequest().permitAll()
                );
        return http.build();
    }
}
