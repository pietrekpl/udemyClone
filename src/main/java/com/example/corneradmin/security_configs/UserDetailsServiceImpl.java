package com.example.corneradmin.security_configs;

import com.example.corneradmin.model.User;
import com.example.corneradmin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;


@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {


    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userService.loadUserByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> {
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.getName());
            authorities.add(authority);
        });

        org.springframework.security.core.userdetails.User userDetail = new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
        return userDetail;
    }
}
