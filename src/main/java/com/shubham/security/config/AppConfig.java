package com.shubham.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
// Import statements
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.shubham.security.repository.UserReposiotory;

@Configuration
public class AppConfig {

    @Autowired
    private UserReposiotory userReposiotory;

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userReposiotory.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}

// @Configuration
// public class AppConfig {

// @Autowired
// private UserReposiotory userRepository;

// @Bean
// public UserDetailsService userDetailsService(){

// return username-> userRepository.findByEmail(username)
// .orElseThrow(()-> new UsernameNotFoundException("User not found"));
// }

// @Bean
// public AuthenticationProvider authenticationProvider(){
// DaoAuthenticationProvider authProvider=new DaoAuthenticationProvider();
// authProvider.setUserDetailsService(userDetailsService());
// authProvider.setPasswordEncoder(passwordEncoder());

// return authProvider;

// }

// // @Bean
// // public AuthenticationManager
// authenticationManager(AuthenticationConfiguration config) throws Exception{

// // return config.getAuthenticationManager();
// // }

// @Bean
// public AuthenticationProvider authenticationProvider(){
// DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
// authProvider.setUserDetailsService(userDetailsService());
// authProvider.setPasswordEncoder(passwordEncoder());

// return authProvider;
// }

// @Bean
// public PasswordEncoder passwordEncoder(){

// return new BCryptPasswordEncoder();
// }

// }
