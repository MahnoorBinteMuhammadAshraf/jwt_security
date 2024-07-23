package com.jwt.service;

import com.jwt.Repo.RoleRepo;
import com.jwt.Repo.UserRepo;
import com.jwt.domain.Role;
import com.jwt.domain.User;
import com.jwt.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("dev")
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepo userRepo;

    @Mock
    private RoleRepo roleRepo;

    @Mock
    private JwtUtil jwtUtil;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private String username;
    private final List<User> users = new ArrayList<>();
    private User user;
    private final List<Role> roles = new ArrayList<>();

    @BeforeEach
    public void setUp()
    {
        Role role1 = new Role((long)1, "USER");
        Role role2 = new Role((long)2, "MANAGER");

        roles.add(role1);
        roles.add(role2);

        user = new User();
        user.setId((long)1);
        user.setUsername("john");
        user.setPassword(passwordEncoder.encode("john password"));
        user.setName("John Smith");
        user.setRoles(roles);

        users.add(user);

        userService.setUsername("john");
    }

    @Test
    public void loadUserByUsernameTest()
    {
        Mockito.when(userRepo.findByUsername(Mockito.anyString())).thenReturn(user);
        userService.loadUserByUsername("username");
    }

    @Test
    public void loadUserByUsernameTest_FoundNull()
    {
        Mockito.when(userRepo.findByUsername(Mockito.anyString())).thenReturn(null);
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () ->   userService.loadUserByUsername("john"));
        assertEquals(exception.getMessage(), "User not found in the database");
    }

    @Test
    public void createUserTest()
    {
        Mockito.when(userRepo.save(Mockito.any(User.class))).thenReturn(user);
        userService.createUser(user);
    }

    @Test
    public void getUserTest()
    {
        Mockito.when(userRepo.findByUsername(Mockito.anyString())).thenReturn(user);
        userService.getUser(Mockito.anyString());
    }

    @Test
    public void getUsersTest()
    {
        Mockito.when(userRepo.findAll()).thenReturn(users);
        userService.getUsers();
    }

    @Test
    public void seeProfileTest()
    {
        Mockito.when(userRepo.findByUsername(Mockito.anyString())).thenReturn(user);
        userService.seeProfile();
    }

    @Test
    public void changeUserNameTest()
    {
        Mockito.when(userRepo.findByUsername(Mockito.anyString())).thenReturn(user);
        Mockito.when(userRepo.save(Mockito.any(User.class))).thenReturn(user);
        userService.changeUserName(Mockito.anyString());
    }

    @Test
    public void changeNameTest()
    {
        Mockito.when(userRepo.findByUsername(Mockito.anyString())).thenReturn(user);
        Mockito.when(userRepo.save(Mockito.any(User.class))).thenReturn(user);
        userService.changeName(username);
    }

    @Test
    public void changePasswordTest()
    {
        Mockito.when(userRepo.findByUsername(Mockito.anyString())).thenReturn(user);
        Mockito.when(userRepo.save(Mockito.any(User.class))).thenReturn(user);

        userService.changePassword(Mockito.anyString());
    }

    @Test
    public void getUsernameTest()
    {
        String username1 = userService.getUsername();
        assertEquals("john", username1);
    }

    @Test
    public void addRoleToUserTest()
    {
        Mockito.when(roleRepo.findByName(Mockito.anyString())).thenReturn(roles.get(1));
        Mockito.when(userRepo.save(Mockito.any(User.class))).thenReturn(user);

        userService.addRoleToUser(user, roles.get(1).getName());
    }
}
