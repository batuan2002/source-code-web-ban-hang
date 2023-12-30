package com.project.fashionshops.configurations;

import com.project.fashionshops.exceptions.DataNotFoundException;
import com.project.fashionshops.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserRepository userRepository;
    // user's detail object
    // khoi tao doi tuong
    @Bean
    public UserDetailsService userDetailsService(){
        // thuoc tinh dau
        return phoneNumber -> userRepository
                    .findByPhoneNumber(phoneNumber)
                    .orElseThrow(() ->
                            new UsernameNotFoundException
                                    ("cannot find user with phone number" + phoneNumber));
        }
        // ma hoa mk vef hsa256
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(); //Đây là bộ mã hóa mật khẩu sử dụng thuật toán băm BCrypt.
    }
    // khoi tao 1 doi tuong AuthenticationProvider
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
    @Bean
    public AuthenticationManager authenticationManager
            (AuthenticationConfiguration config) throws  Exception {
        return config.getAuthenticationManager();
    }
    }

