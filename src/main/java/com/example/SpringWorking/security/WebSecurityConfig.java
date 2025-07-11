package com.example.SpringWorking.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.SpringWorking.util.constants.Privillages;
import com.example.SpringWorking.util.constants.Roles;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class WebSecurityConfig {
    private static final String[] WHITELIST = {
            "/",
            "/home",
            "/db-console/**",
            "/login",
            "/register",
            "/css/**",
            "/js/**",
            "/fonts/**",
            "/images/**",
            "/sample",
            "/test",
            "/forgot-password",
             "/reset-password",
             "/change-password"
            
    };

    // Password Encoder Bean
    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Security Filter Chain Bean
    @SuppressWarnings("removal")
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authz -> authz
                .requestMatchers(WHITELIST).permitAll() 
                .requestMatchers("/profile/**").authenticated()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/editor/**").hasAnyRole("ADMIN","EDITOR")
                .requestMatchers("/admin/**").hasAuthority(Privillages.ACCESS_ADMIN_PANEL.getprivillageName())
                // Public paths
                .anyRequest().authenticated()  // All other requests require authentication
            )
            .formLogin(form -> form
                .loginPage("/login")  // Custom login page URL
                .loginProcessingUrl("/login")  // Custom login form submission URL
                .usernameParameter("username")  // Username parameter name
                .passwordParameter("password")  // Password parameter name
                .defaultSuccessUrl("/home", true)  // Redirect after successful login
                .failureUrl("/login?error")  // Redirect on login failure
                .permitAll()  // Allow login page without authentication
            )
            .logout(logout -> logout
                .logoutUrl("/logout")  // Custom logout URL
                .logoutSuccessUrl("/home")  // Redirect after logout
            )
            .rememberMe().rememberMeParameter("remember-me")
            .and()
            .httpBasic();  // Enable basic authentication for all other requests

        // Disable CSRF and configure headers for H2 console (if needed)
        http.csrf(csrf -> csrf.disable())  // Disable CSRF protection (adjust based on your needs)
            .headers(headers -> headers.frameOptions().sameOrigin());  // Allow iframe requests from the same origin

        return http.build();
    }
}

