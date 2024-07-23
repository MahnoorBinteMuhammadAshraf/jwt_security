package com.jwt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JwtApplication {

    public static void main(String[] args) {
        SpringApplication.run(JwtApplication.class, args);
    }

//    @Bean
//    CommandLineRunner run(UserService userService)
//    {
//        return args -> {
//            userService.addRoleToUser("john", "ROLE_MANAGER");
//            userService.addRoleToUser("john", "ROLE_ADMIN");
//            userService.addRoleToUser("jorge", "ROLE_USER");
//        };
//    }

}
