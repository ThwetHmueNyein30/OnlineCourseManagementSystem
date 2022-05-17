package com.thn.onlinecoursemanagement.controller;

import com.thn.onlinecoursemanagement.payload.response.BaseResponse;
import com.thn.onlinecoursemanagement.entities.Role;
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
        BaseResponse response = new BaseResponse();
        response.setDateTime(LocalDateTime.now());
        try {
            response.setStatus(true);
            response.setResult(roleRepository.findAll());
            response.setMessage("Success");
        } catch (Exception e) {
            response.setMessage("Fail");
            log.error("Exception : " + e);
        }
        return response;
    }

    @PostMapping()
    @CrossOrigin
    @Secured("ROLE_ADMIN")
    BaseResponse createRole(@RequestBody Role role) {
        BaseResponse response = new BaseResponse();
        response.setDateTime(LocalDateTime.now());
        if (role == null) {
            response.setStatus(false);
            response.setMessage("No role data found!");
            return response;
        }
        try {
            role = roleRepository.save(role);
            response.setStatus(true);
            response.setResult(role);
            response.setMessage("Success");
        } catch (Exception e) {
            response.setStatus(false);
            response.setMessage("Fail");
            log.error("Exception : " + e);
        }
        return response;
    }


    @PutMapping("{id}")
    @CrossOrigin
    @Secured("ROLE_ADMIN")
    BaseResponse updateRole(@PathVariable Long id, @RequestBody Role role) {
        BaseResponse response = new BaseResponse();
        response.setDateTime(LocalDateTime.now());
        if (role == null) {
            response.setMessage("No Request Body");
            return response;
        }
        try {
            Optional<Role> optionalRole = roleRepository.findById(id);
            if (optionalRole.isPresent()) {
                response.setStatus(true);
                Role r = optionalRole.get();
                r.setRoleId(role.getRoleId());
                r.setName(r.getName());
                response.setResult(r);
                response.setMessage("Successfully updated");
            } else {
                response.setStatus(false);
                response.setMessage("Failed to update");
            }
        } catch (Exception e) {
            response.setStatus(false);
            response.setMessage("Failed to update");
            log.info("Exception : ", e);

        }
        return response;
    }

    @DeleteMapping("{id}")
    @CrossOrigin
    @Secured("ROLE_ADMIN")
    BaseResponse deleteRole(@PathVariable Long id) {
        BaseResponse response = new BaseResponse();
        response.setDateTime(LocalDateTime.now());

        try {
            Optional<Role> optionalRole = roleRepository.findById(id);
            if (optionalRole.isPresent()) {
                response.setStatus(true);
                Role role = optionalRole.get();
                roleRepository.deleteById(id);
                response.setResult(role);
                response.setMessage("Successfully deleted");
            } else {
                response.setStatus(false);
                response.setResult("There is no data with that ID.");
            }
        } catch (Exception e) {
            response.setStatus(false);
            response.setResult("Fail");
            log.info("Exception : ", e);

        }
        return response;
    }

}
