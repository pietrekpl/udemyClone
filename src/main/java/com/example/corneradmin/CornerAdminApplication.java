package com.example.corneradmin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;


@SpringBootApplication
@EnableMethodSecurity(securedEnabled = true)
public class CornerAdminApplication  {

    public static void main(String[] args) {
        SpringApplication.run(CornerAdminApplication.class, args);
    }




}
