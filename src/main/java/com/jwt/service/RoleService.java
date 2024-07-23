package com.jwt.service;

import com.jwt.Repo.RoleRepo;
import com.jwt.Repo.UserRepo;
import com.jwt.domain.Role;
import com.jwt.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class RoleService {
    public final RoleRepo roleRepo;
    private final UserRepo userRepo;

    @Autowired
    public RoleService(RoleRepo roleRepo, UserRepo userRepo) {
        this.roleRepo = roleRepo;
        this.userRepo = userRepo;
    }

    public Role getRole(String roleName) {
        return roleRepo.findByName("ROLE_" + roleName);
    }

    public List<Role> getAllRoles() {
        return roleRepo.findAll();
    }

    public Role createRole(Role role) {
        return roleRepo.save(role);
    }

    public Role updateRole(String preName, String newName) {
        Role role = roleRepo.findByName("ROLE_" + preName);
        log.info("role is {}", role);
        role.setName("ROLE_" + newName);
        return roleRepo.save(role);
    }

    public void deleteRole(String roleName) {
        roleRepo.delete(roleRepo.findByName("ROLE_" + roleName));
    }

    public List<User> getUsersByRole(Role role) {
        return userRepo.findAllByRoles(role);
    }

   public Integer getUserCount(String roleName) {
        return userRepo.countAllByRoles(roleRepo.findByName("ROLE_" + roleName));
   }
}
