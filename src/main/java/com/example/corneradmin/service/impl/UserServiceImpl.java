package com.example.corneradmin.service.impl;


import com.example.corneradmin.model.Role;
import com.example.corneradmin.model.User;
import com.example.corneradmin.repository.RoleRepository;
import com.example.corneradmin.repository.UserRepository;
import com.example.corneradmin.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public User loadUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User createUser(String email, String password) {
        String encodedPassword = passwordEncoder.encode(password);
        return userRepository.save( new User(email, encodedPassword));
    }

    @Override
    public void assignRoleToUser(String email, String roleName) {
    User user = loadUserByEmail(email);
    Role role = roleRepository.findByName(roleName);
    user.assignRoleToUser(role);
    }
}
