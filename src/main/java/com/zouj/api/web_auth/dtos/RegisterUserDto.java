package com.zouj.api.web_auth.dtos;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;


public class RegisterUserDto {
    private String id;

    @NotEmpty(message = "The email is required.") 
    @Email(message = "The email is not a valid email.") 
    private String email;

    @NotBlank(message = "The password is required.") 
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!*()]).{8,}$", message = "Password must be 8 characters long and combination of uppercase letters, lowercase letters, numbers, special characters.") 
    private String password;

    @Size(min = 3, max = 20, message = "The fullname must be from 3 to 20 characters.") 
    private String fullname;

    public RegisterUserDto() {}

    public RegisterUserDto(String email, String password, String fullname) {
        this.email = email;
        this.password = password;
        this.fullname = fullname;
    }


    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }


    
}
