package com.shubham.security.service;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.shubham.security.config.JwtService;
import com.shubham.security.entity.UserRoles;
import com.shubham.security.entity.Users;
import com.shubham.security.model.AuthenticationRequest;
import com.shubham.security.model.AuthenticationResponse;
import com.shubham.security.model.RegisterRequest;
import com.shubham.security.repository.UserReposiotory;
import com.shubham.security.repository.UserRoleRepository;

import lombok.RequiredArgsConstructor;

@Service
// @RequiredArgsConstructor
public class AuthenticationService {

    private static final Logger logger = LogManager.getLogger(AuthenticationService.class);
    @Autowired
    private UserReposiotory userReposiotory;
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {

        AuthenticationResponse returnValue = new AuthenticationResponse();

        try {
            Users user = new Users();

            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());
            user.setPassword(passwordEncoder.encode(request.getPassword()));

            String role = StringUtils.hasText(request.getUserRole()) ? request.getUserRole() : "USER";

            Optional<UserRoles> userRoles = userRoleRepository.findByRoleName(role);

            if (userRoles.isPresent()) {

                user.setRoles(userRoles.get());
            } else {
                UserRoles newRole = new UserRoles();

                newRole.setRoleName(role);
                userRoleRepository.save(newRole);
                user.setRoles(newRole);
            }

            userReposiotory.save(user);

            String jwtToken = jwtService.generateToken(user);

            returnValue.setToken(jwtToken);

        } catch (Exception e) {
            logger.info("Error While Registering New User : {} {}", e.getMessage(), e);
            returnValue.setToken(null);

        }

        return returnValue;

    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        AuthenticationResponse returnValue = new AuthenticationResponse();
        String jwtToken = null;

        try {

            authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
            Optional<Users> user = userReposiotory.findByEmail(request.getEmail());

            if (user.isPresent()) {
                jwtToken = jwtService.generateToken(user.get());
            }

            returnValue.setToken(jwtToken);

        } catch (Exception e) {
            logger.info("Error While Authenticationg User : {} {}", e.getMessage(), e);
            returnValue.setToken(null);
        }

        return returnValue;
    }

}
