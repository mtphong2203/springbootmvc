package com.maiphong.springbootmvc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Bean
    public PasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/auth/**").permitAll() // allow access to login/register page
                        .requestMatchers("/categories").permitAll() // allow access to home page
                        .requestMatchers("/products").permitAll() // allow access to home page
                        .requestMatchers("/css/**", "/js/**", "/images/**").permitAll() // allow access to static
                                                                                        // resources

                        .requestMatchers("/categories/**").hasRole("ADMIN")
                        .requestMatchers("/products/**").hasAnyRole("ADMIN", "USER")
                        .anyRequest().authenticated()) // protect all other requests
                .formLogin(formLogin -> formLogin
                        .loginPage("/auth/login")
                        .loginProcessingUrl("/auth/login")
                        .defaultSuccessUrl("/categories") // After successful login, redirect to home page
                        .failureUrl("/auth/login?error")) // After failed login, redirect to login page with error
                .logout(LogoutConfigurer::permitAll)
                .exceptionHandling(
                        exceptionHandling -> exceptionHandling.accessDeniedPage("/auth/access-denied"));
        return http.build();
    }
}
