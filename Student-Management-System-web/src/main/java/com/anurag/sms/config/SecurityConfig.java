package com.anurag.sms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.anurag.sms.service.CustomUserDetailsService;

@Configuration
public class SecurityConfig {

    /**
     * Password Encoder Bean
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Security Filter Chain
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http

                .authorizeHttpRequests(auth -> auth

                        // Public URLs
                        .requestMatchers(
                                "/",
                                "/login",
                                "/register",
                                "/css/**",
                                "/js/**",
                                "/images/**")
                        .permitAll()

                        // ---- Role rules (#3) -------------------------------------
                        // The FIRST matching rule wins, so specific rules come before
                        // general ones. Every change is a POST, and every add/edit
                        // form is GET /<module>/new or /<module>/edit/{id}.

                        // Fees: admins only, viewing included
                        .requestMatchers("/fee", "/fee/**")
                        .hasRole("ADMIN")

                        // Attendance, exams and results: teachers may change them too
                        .requestMatchers(
                                "/attendance/new", "/attendance/edit/**", "/attendance/bulk",
                                "/exam/new", "/exam/edit/**",
                                "/result/new", "/result/edit/**")
                        .hasAnyRole("ADMIN", "TEACHER")
                        .requestMatchers(HttpMethod.POST,
                                "/attendance/**", "/exam/**", "/result/**")
                        .hasAnyRole("ADMIN", "TEACHER")

                        // Students, teachers, courses, subjects and enrollments:
                        // anyone signed in may view, only admins may change
                        .requestMatchers("/*/new", "/*/edit/**",
                                "/student/import", "/student/export")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,
                                "/student", "/student/**",
                                "/teacher", "/teacher/**",
                                "/course", "/course/**",
                                "/subject", "/subject/**",
                                "/enrollment", "/enrollment/**")
                        .hasRole("ADMIN")

                        // Every other request requires login
                        .anyRequest()
                        .authenticated())

                .formLogin(form -> form

                        .loginPage("/login")

                        .loginProcessingUrl("/login")

                        .defaultSuccessUrl("/dashboard", true)

                        .failureUrl("/login?error=true")

                        .permitAll())

                .logout(logout -> logout

                        .logoutUrl("/logout")

                        .logoutSuccessUrl("/login?logout=true")

                        .permitAll());

        return http.build();
    }

    /**
     * Authentication Manager
     */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration)
            throws Exception {

        return configuration.getAuthenticationManager();
    }

    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

}