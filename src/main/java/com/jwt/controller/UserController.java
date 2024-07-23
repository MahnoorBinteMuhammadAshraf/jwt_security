package com.jwt.controller;

import com.jwt.Repo.UserRepo;
import com.jwt.domain.Role;
import com.jwt.domain.User;
import com.jwt.service.TokenBlackList;
import com.jwt.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Map;

@Slf4j
@RequestMapping(value = "/user")
@RestController
public class UserController
{
    private final UserService userService;
    private final UserRepo userRepo;
    private final TokenBlackList tokenBlackList;

    @Autowired
    public UserController(UserService userService, UserRepo userRepo, TokenBlackList tokenBlackList)
    {
        this.userService = userService;
        this.userRepo = userRepo;
        this.tokenBlackList = tokenBlackList;
    }

    @GetMapping(value = "/profile")
    public ResponseEntity<User> seeProfile()
    {
        User user = userService.seeProfile();
        return ResponseEntity.ok(user);
    }

    @PutMapping(value = "/change_username")
    public ResponseEntity<User> changeUsername(@RequestParam("update_username") String updateName)
    {
        User user = userService.changeUserName(updateName);
        return ResponseEntity.ok(user);
    }

    @PutMapping(value = "/change_name")
    public ResponseEntity<User> changeYourName(@RequestParam("update_name") String updateName)
    {
        User user = userService.changeName(updateName);
        return ResponseEntity.ok(user);
    }

    @PutMapping(value = "/change_password")
    public ResponseEntity<?> changePassword(@RequestHeader("Authorization") String token, @RequestParam("new_password") String newPassword)
    {
        Map<String, String> user = userService.changePassword(newPassword);
        tokenBlackList.addToBlacklist(token.substring(7));

        return ResponseEntity.ok(user);
    }

    @PostMapping(value="/add_role")
    public ResponseEntity<Collection<Role>> addRoleToUser(@RequestParam("role_name") String roleName) {
        User user = userRepo.findByUsername(userService.getUsername());
        return ResponseEntity.ok(userService.addRoleToUser(user, roleName));
    }

    @GetMapping(value = "/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String token)
    {
        log.info("token is {}", token);
        tokenBlackList.addToBlacklist(token.substring(7));

        return ResponseEntity.ok("Logged out Successfully!");
    }
}
