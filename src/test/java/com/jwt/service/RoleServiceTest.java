package com.jwt.service;

import com.jwt.Repo.RoleRepo;
import com.jwt.Repo.UserRepo;
import com.jwt.domain.Role;
import com.jwt.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("dev")
@ExtendWith(MockitoExtension.class)
public class RoleServiceTest {
    @InjectMocks
    private RoleService roleService;

    @Mock
    public RoleRepo roleRepo;
    @Mock
    private UserRepo userRepo;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final List<User> users = new ArrayList<>();
    private User user;
    private final List<Role> roles = new ArrayList<>();

    @BeforeEach
    public void setUp() {
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
    }

    @Test
    public void getRoleTest()
    {
        Mockito.when(roleRepo.findByName(Mockito.anyString())).thenReturn(roles.get(0));
        Role role = roleService.getRole(roles.get(0).getName());
        assertEquals(roles.get(0),role);
    }

    @Test
    public void getAllRolesTest()
    {
        Mockito.when(roleRepo.findAll()).thenReturn(roles);
        List<Role> rolez = roleService.getAllRoles();
        assertEquals(roles,rolez);
    }

    @Test
    public void createRoleTest()
    {
        Mockito.when(roleRepo.save(Mockito.any())).thenReturn(roles.get(0));
        Role r = roleService.createRole(roles.get(0));
        assertEquals(roles.get(0),r);
    }

    @Test
    public void updateRoleTest()
    {
        Mockito.when(roleRepo.findByName(Mockito.anyString())).thenReturn(roles.get(0));
        Mockito.when(roleRepo.save(Mockito.any())).thenReturn(roles.get(0));

        Role r = roleService.updateRole(roles.get(0).getName(), Mockito.anyString());
        assertEquals(roles.get(0),r);
    }

    @Test
    public void deleteRoleTest()
    {
        Mockito.when(roleRepo.findByName(Mockito.anyString())).thenReturn(roles.get(0));
        roleService.deleteRole(roles.get(0).getName());
    }

    @Test
    public void getUsersByRoleTest() {
        Mockito.when(userRepo.findAllByRoles(Mockito.any())).thenReturn(users);
        List<User> userz = roleService.getUsersByRole(roles.get(0));
        assertEquals(users,userz);
    }

    @Test
    public void getUserCountTest() {
        Mockito.when(userRepo.countAllByRoles(Mockito.any())).thenReturn(users.size());
        int count = roleService.getUserCount("ROLE_MANAGER");
        assertEquals(users.size(),count);
    }
}
