package com.example.corneradmin.service;

import com.example.corneradmin.model.User;

public interface UserService {

User loadUserByEmail(String email);

User createUser(String email, String password);

void assignRoleToUser(String email, String RoleName);

}
