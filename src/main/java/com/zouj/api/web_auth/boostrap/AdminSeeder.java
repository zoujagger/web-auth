package com.zouj.api.web_auth.boostrap;

import java.util.Optional;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.zouj.api.web_auth.dtos.RegisterUserDto;
import com.zouj.api.web_auth.entities.Role;
import com.zouj.api.web_auth.entities.RoleEnum;
import com.zouj.api.web_auth.entities.User;
import com.zouj.api.web_auth.repositories.RoleRepository;
import com.zouj.api.web_auth.repositories.UserRepository;

@Component
public class AdminSeeder implements ApplicationListener<ContextRefreshedEvent> {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public AdminSeeder(
            RoleRepository roleRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        this.createSuperAdministrator();
    }

    private void createSuperAdministrator() {
        RegisterUserDto userDto = new RegisterUserDto("superadmin42@gmail.com", "supperAdmin123pass!", "Super Admin");

        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.SUPER_ADMIN);
        Optional<User> optionalUser = userRepository.findByEmail(userDto.email());

        if (optionalRole.isEmpty() || optionalUser.isPresent()) {
            return;
        }

        User user = new User();
        user.setFullname(userDto.fullname());
        user.setEmail(userDto.email());
        user.setPassword(passwordEncoder.encode(userDto.password()));
        user.setRole(optionalRole.get());

        userRepository.save(user);
    }
}
