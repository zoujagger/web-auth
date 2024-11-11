package com.zouj.api.web_auth.services;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.zouj.api.web_auth.dtos.LoginUserDto;
import com.zouj.api.web_auth.dtos.RegisterUserDto;
import com.zouj.api.web_auth.entities.Role;
import com.zouj.api.web_auth.entities.RoleEnum;
import com.zouj.api.web_auth.entities.Token;
import com.zouj.api.web_auth.entities.User;
import com.zouj.api.web_auth.repositories.EmailSender;
import com.zouj.api.web_auth.repositories.RoleRepository;
import com.zouj.api.web_auth.repositories.TokenRepository;
import com.zouj.api.web_auth.repositories.UserRepository;

import jakarta.transaction.Transactional;

import java.util.UUID;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final RoleRepository roleRepository;
    private final TokenService tokenService;
    private final EmailSender emailSender;

    public AuthenticationService(
            UserRepository userRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder,
            RoleRepository roleRepository,
            TokenService tokenService,
            EmailSender emailSender) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.tokenService = tokenService;
        this.emailSender = emailSender;
    }

    public String signup(RegisterUserDto input) {
        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.USER);
        Optional<User> optionalUser = userRepository.findByEmail(input.getEmail());

        if (optionalRole.isEmpty()) {
            return null;
        }

        if (optionalUser.isPresent()) {
            throw new IllegalStateException("User already exist");
        }

        User user = new User();
        user.setFullname(input.getFullname());
        user.setEmail(input.getEmail());
        user.setPassword(passwordEncoder.encode(input.getPassword()));
        user.setRole(optionalRole.get());
        userRepository.save(user);

        String token = UUID.randomUUID().toString();
        Token confirmationToken = new Token(
                token,
                LocalDateTime.now().plusMinutes(10),
                user);

        tokenService.saveConfirmationToken(confirmationToken);
        // this.sendConfirmationTokenEmail(token);
        String confirmationLink = "http://localhost:8005/auth/confirmation?token=" + token;
        emailSender.send(input.getEmail(), buildEmail(input.getFullname(), confirmationLink));

        return token;
    }

    public User authenticate(LoginUserDto input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.email(),
                        input.password()));

        return userRepository.findByEmail(input.email())
                .orElseThrow();
    }

    // private String sendConfirmationTokenEmail(String token) {
    //     // todo: send the email.
    //     System.out.println("email sending ok ok ");

    //     return token;
    // }

    @Transactional
    public String confirmToken(String token) {
        String loginLink = "http://localhost:8005/auth/login";
        Token confirmationToken = tokenService
                .getToken(token)
                .orElseThrow(() -> new IllegalStateException("token not found"));

        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException(buildConfirmedPage(loginLink, "Email already confirmed."));
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("token expired");
        }

        tokenService.setConfirmedAt(token);
        userRepository.enableAppUser(
                confirmationToken.getAppUser().getEmail());
        return buildConfirmedPage(loginLink, "Email Confirmed");
    }

    private String buildEmail(String name, String link) {
        return "<p> Hi " + name +
                ",</p>" +
                "<p> Thank you for registering. Please click on the below link to activate your account: </p>" +
                "<blockquote><p> <a href=\"" + link
                + "\">Activate Now</a> </p></blockquote>\n Link will expire in 10 minutes.";
    }

    private String buildConfirmedPage(String link, String message) {
        return "<p> " + message + ". Please click on the below link to Login: </p>" +
                "<blockquote><p> <a href=\"" + link + "\">Login</a> </p>";
    }

}
