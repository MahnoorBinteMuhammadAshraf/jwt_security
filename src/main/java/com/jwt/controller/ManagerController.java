package com.jwt.controller;

import com.jwt.Repo.RoleRepo;
import com.jwt.domain.Role;
import com.jwt.domain.User;
import com.jwt.service.RoleService;
import com.jwt.service.TokenBlackList;
import com.jwt.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/manager")
public class ManagerController {
    private final RoleRepo roleRepo;
    private final TokenBlackList tokenBlackList;
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public ManagerController(UserService userService, RoleRepo roleRepo, TokenBlackList tokenBlackList, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
        this.tokenBlackList = tokenBlackList;
        this.roleRepo = roleRepo;
    }

    @GetMapping(value = "/all_users")
    public ResponseEntity<List<User>> getUsers()
    {
        return ResponseEntity.ok(userService.getUsers());
    }

    @GetMapping(value = "/all_roles")
    public ResponseEntity<List<Role>> getRoles()
    {
        return ResponseEntity.ok(roleService.getAllRoles());
    }

    @PostMapping(value = "/save_role")
    public ResponseEntity<Role> saveRole(@RequestBody Role role)
    {
        return ResponseEntity.ok(roleService.createRole(role));
    }

    @PutMapping(value = "/update_role")
    public ResponseEntity<?> updateRole(@RequestParam("previous") String previous, @RequestParam("new") String next)
    {
        return ResponseEntity.ok(roleService.updateRole(previous, next));
    }

    @GetMapping(value = "/getSpecifiedRoleUsers")
    public ResponseEntity<List<User>> getSpecifiedUsers(@RequestParam("role") String roleName)
    {
        Role role = roleRepo.findByName("ROLE_"+roleName);
        return ResponseEntity.ok(roleService.getUsersByRole(role));
    }

    @GetMapping(value = "/countUsersRoleWise")
    public ResponseEntity<?> getCount(@RequestParam("role") String role)
    {
        return ResponseEntity.ok("The total count is " + roleService.getUserCount(role));
    }

    @DeleteMapping(value = "/deleteRole")
    public ResponseEntity<?> deleteRole(@RequestParam("role") String role)
    {
        roleService.deleteRole(role);
        return ResponseEntity.ok("Role deleted successfully");
    }

    @GetMapping(value = "/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String token)
    {
        log.info("token is {}", token);
        tokenBlackList.addToBlacklist(token.substring(7));

        return ResponseEntity.ok("Logged out Successfully!");
    }

    /*@PostMapping("/save_roles")
    public ResponseEntity<Role> saveRole(@RequestBody Role role)
    {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/users/save").toUriString());
        return ResponseEntity.created(uri).body(roleRepo.save(role));
    }*/
}
