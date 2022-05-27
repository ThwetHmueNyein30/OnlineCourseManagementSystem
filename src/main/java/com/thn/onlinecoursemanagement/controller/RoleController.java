package com.thn.onlinecoursemanagement.controller;

import com.thn.onlinecoursemanagement.entities.Role;
import com.thn.onlinecoursemanagement.payload.response.BaseResponse;
import com.thn.onlinecoursemanagement.repositories.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author ThwetHmueNyein
 * @Date 02/05/2022
 */

@Slf4j
@RestController
@RequestMapping("role")
public class RoleController {
    final RoleRepository roleRepository;

    public RoleController(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @GetMapping
    @CrossOrigin
    @Secured("ROLE_ADMIN")
    BaseResponse getRole() {
        try {
            return new BaseResponse(true, roleRepository.findAll(),LocalDateTime.now(),"Successful");
        } catch (Exception e) {
            return new BaseResponse(false,null,LocalDateTime.now(),"Fail");
        }
    }

    @PostMapping()
    @CrossOrigin
    @Secured("ROLE_ADMIN")
    BaseResponse createRole(@RequestBody Role role) {
        if (role == null) {
            return new BaseResponse(false,null,LocalDateTime.now(),"No role data found");
        }
        try {
            role = roleRepository.save(role);
            return new BaseResponse(true,role,LocalDateTime.now(),"Successfully created");
        } catch (Exception e) {
            return new BaseResponse(false,null,LocalDateTime.now(),"Fail");
        }
    }


    @PutMapping("{id}")
    @CrossOrigin
    @Secured("ROLE_ADMIN")
    BaseResponse updateRole(@PathVariable Long id, @RequestBody Role role) {
        if (role == null) {
            return new BaseResponse(false,null,LocalDateTime.now(),"No Request Body");
        }
        try {
            Optional<Role> optionalRole = roleRepository.findById(id);
            if (optionalRole.isPresent()) {
                Role r = optionalRole.get();
                r.setRoleId(role.getRoleId());
                r.setName(r.getName());
                r= roleRepository.save(r);
                return new BaseResponse(true,r,LocalDateTime.now(),"Successfully updated");
            } else {
                return new BaseResponse(false,null,LocalDateTime.now(),"No Role with that ID");
            }
        } catch (Exception e) {
            return new BaseResponse(false,null,LocalDateTime.now(),"Fail to update");
        }
    }

    @DeleteMapping("{id}")
    @CrossOrigin
    @Secured("ROLE_ADMIN")
    BaseResponse deleteRole(@PathVariable Long id) {
        try {
            Optional<Role> optionalRole = roleRepository.findById(id);
            if (optionalRole.isPresent()) {
                Role role = optionalRole.get();
                roleRepository.deleteById(id);
                return new BaseResponse(true,role,LocalDateTime.now(),"Successfully deleted");

            } else {
                return new BaseResponse(false,null,LocalDateTime.now(),"No Role with that ID");
            }
        } catch (Exception e) {
            return new BaseResponse(false,null,LocalDateTime.now(),"Fail to delete");
        }
    }

}
