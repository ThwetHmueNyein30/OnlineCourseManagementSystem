package com.thn.onlinecoursemanagement.repositories;

import com.thn.onlinecoursemanagement.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author ThwetHmueNyein
 * @Date 02/05/2022
 */
@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByRoleId(Long roleId);
}
