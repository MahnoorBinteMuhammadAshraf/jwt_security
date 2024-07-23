package com.jwt.service;

import com.jwt.Repo.RoleRepo;
import com.jwt.Repo.UserRepo;
import com.jwt.domain.Role;
import com.jwt.domain.User;
import com.jwt.utils.JwtUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class UserService implements UserDetailsService {
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final JwtUtil jwtUtil;
    @Setter
    @Getter
    private String username;

    @Autowired
    public UserService(UserRepo userRepo, RoleRepo roleRepo, JwtUtil jwtUtil) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);
        if (user == null) {
            log.error("User not found in the database");
            throw new UsernameNotFoundException("User not found in the database");
        }
        else{
            log.info("User found in the database");
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });
        return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(), authorities);
    }

    public User createUser(User user)
    {
        log.info("Saving user {}", user.getUsername());
        user.setRoles(addRoleToUser(user, "ROLE_USER"));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    public User getUser(String username)
    {
        log.info("Getting user {}", username);
        return userRepo.findByUsername(username);
    }

    public List<User> getUsers()
    {
        return userRepo.findAll();
    }

    // user can perform below operations
    public User seeProfile()
    {
        log.info("Getting profile of user {}", username);
        return userRepo.findByUsername(this.username);
    }

    public User changeUserName(String newName)
    {
        log.info("Change username {} to {}", this.username, newName);
        User user = userRepo.findByUsername(this.username);
        user.setUsername(newName);
        userRepo.save(user);
        return user;
    }

    public User changeName(String name)
    {
        User user = userRepo.findByUsername(this.username);
        log.info("Change name {} to {}", user.getName(), name);
        user.setName(name);
        userRepo.save(user);
        return user;
    }

    public Map<String, String> changePassword(String password)
    {
        User user = userRepo.findByUsername(this.username);
        log.info("Change password {} to {}", user.getPassword(), password);
        user.setPassword(passwordEncoder.encode(password));
        userRepo.save(user);

        /* generate new token because password renew kiya hai */
        Map<String, String> tokens = new HashMap<>();
        String token = jwtUtil.generateToken(user.getUsername());
        tokens.put("access-token", token);
        return tokens;
    }

    public Collection<Role> addRoleToUser(User user, String rolename)
    {
        log.info("Adding role {} to user {}", rolename, user);
        String roleCheck = "ROLE_" + rolename;
        Role role = roleRepo.findByName(roleCheck);

        if(role == null)
            role = roleRepo.save(new Role());

        log.info("the role is : {}", role);
        user.getRoles().add(role);
        userRepo.save(user);
        return user.getRoles();
    }
}
