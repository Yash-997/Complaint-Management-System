package com.yash.Complaint_Management_System.service;

import com.yash.Complaint_Management_System.dto.LoginRequestDto;
import com.yash.Complaint_Management_System.dto.UserRequestDto;
import com.yash.Complaint_Management_System.entity.User;
import com.yash.Complaint_Management_System.exception.ResourceAlreadyExistsException;
import com.yash.Complaint_Management_System.exception.ResourceNotFoundException;
import com.yash.Complaint_Management_System.repository.UserRepository;
import com.yash.Complaint_Management_System.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil,
            AuthenticationManager authenticationManager
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    // REGISTER USER
    public Map<String, Object> registerUser(UserRequestDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new ResourceAlreadyExistsException(
                    "Email already registered: " + dto.getEmail()
            );
        }
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(
                passwordEncoder.encode(dto.getPassword())
        );
        // DEFAULT ROLE
        if(dto.getEmail().equals("admin@gmail.com")
                || dto.getEmail().equals("yashdabhade.official@gmail.com")) {

            user.setRole("ROLE_ADMIN");
        } else {
            user.setRole("ROLE_USER");
        }

        User savedUser = userRepository.save(user);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "User registered successfully");
        response.put("userId", savedUser.getId());
        response.put("name", savedUser.getName());
        response.put("email", savedUser.getEmail());
        response.put("role", savedUser.getRole());

        return response;
    }

    // LOGIN USER
    public Map<String, Object> loginUser(LoginRequestDto dto) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.getEmail(),
                        dto.getPassword()
                )
        );
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found")
                );

        String token = jwtUtil.generateToken(user.getEmail());

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("userId", user.getId());
        response.put("name", user.getName());
        response.put("email", user.getEmail());
        response.put("role", user.getRole());

        return response;
    }

    // GET USER BY ID
    public User getUserById(Long id) {

        return userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id: " + id
                        )
                );
    }

    // GET USER BY EMAIL
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with email: " + email
                        )
                );
    }
}