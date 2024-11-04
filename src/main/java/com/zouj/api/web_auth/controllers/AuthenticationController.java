package com.zouj.api.web_auth.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zouj.api.web_auth.dtos.LoginResponse;
import com.zouj.api.web_auth.dtos.LoginUserDto;
import com.zouj.api.web_auth.dtos.RegisterUserDto;
import com.zouj.api.web_auth.entities.User;
import com.zouj.api.web_auth.services.AuthenticationService;
import com.zouj.api.web_auth.services.JwtService;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {

    private final JwtService jwtService;

    private final AuthenticationService authenticationService;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<User> register(@RequestBody RegisterUserDto registerUserDto) {
        System.out.println("ENter signu \n" + registerUserDto.toString());

        User registeredUser = authenticationService.signup(registerUserDto);
        System.out.println(registeredUser);

        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto) {
        User authenticatedUser = authenticationService.authenticate(loginUserDto);

        String jwtToken = jwtService.generateToken(authenticatedUser);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(jwtToken);
        loginResponse.setExpiresIn(jwtService.getExpirationTime());
        System.out.println(loginResponse);

        return ResponseEntity.ok(loginResponse);
    }

}
