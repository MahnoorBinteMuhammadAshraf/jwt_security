package com.jwt.Repo;

import com.jwt.domain.Role;
import com.jwt.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface  UserRepo extends JpaRepository<User, Long> {
    public User findByUsername(String username);

    List<User> findAllByRoles(Role role);

    int countAllByRoles(Role role);

//    @Query(value = "SELECT u.* FROM USER u JOIN USER_ROLES ur ON u.id = ur.user_id JOIN ROLE r ON ur.roles_id = r.id WHERE r.name = :roleName", nativeQuery = true)
//    List<User> findUsersByRole(@Param("roleName") String roleName); // use or ORM functions
}
