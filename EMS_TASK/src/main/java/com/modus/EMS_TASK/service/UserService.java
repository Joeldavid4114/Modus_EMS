package com.modus.EMS_TASK.service;

import com.modus.EMS_TASK.JWTUtil.JwtService;
import com.modus.EMS_TASK.dto.LoginDto;
import com.modus.EMS_TASK.dto.UserRegistrationDto;
import com.modus.EMS_TASK.entity.Role;
import com.modus.EMS_TASK.entity.User;
import com.modus.EMS_TASK.repository.RoleRepository;
import com.modus.EMS_TASK.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;

    public void register(UserRegistrationDto dto) {
        if (userRepo.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("Email already in use");
        }
        if (userRepo.findByUsername(dto.getUsername()).isPresent()) {
            throw new RuntimeException("Username already taken");
        }

        // Create user
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(encoder.encode(dto.getPassword()));
        user.setEnabled(true);

        // Assign role: first user is ADMIN, others are EMPLOYEE
        String roleName = userRepo.count() == 0 ? "ADMIN" : "EMPLOYEE";
        Role role = roleRepo.findByName(roleName).orElseGet(() -> {
            Role newRole = new Role();
            newRole.setName(roleName);
            return roleRepo.save(newRole);
        });

        user.getRoles().add(role);
        userRepo.save(user);
    }

    public String login(LoginDto dto) {
        Optional<User> optionalUser = userRepo.findByEmail(dto.getEmail());
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User user = optionalUser.get();
        if (!encoder.matches(dto.getPassword(), user.getPassword())) {
            throw new RuntimeException("Wrong password");
        }

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (Role role : user.getRoles()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
        }

        UserDetails springUser = new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                authorities
        );

        return jwtService.generateToken(springUser);
    }
}
