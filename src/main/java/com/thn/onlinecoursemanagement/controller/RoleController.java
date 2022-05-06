package com.thn.onlinecoursemanagement.controller;

import com.thn.onlinecoursemanagement.payload.response.BaseResponse;
import com.thn.onlinecoursemanagement.entities.Role;
import com.thn.onlinecoursemanagement.repositories.RoleRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author ThwetHmueNyein
 * @Date 02/05/2022
 */

@RestController
@RequestMapping("role")
public class RoleController {
    final RoleRepository roleRepository;

    public RoleController(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @GetMapping
    BaseResponse getRole() {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setStatus(true);
        baseResponse.setDateTime(LocalDateTime.now());
        baseResponse.setResult(roleRepository.findAll());
        return baseResponse;
    }

    @PostMapping()
    BaseResponse createRole(@RequestBody Role role) {
        BaseResponse response = new BaseResponse();
        response.setStatus(true);
        response.setDateTime(LocalDateTime.now());
        if(role==null){
            response.setMessage("No role data found!");
            return response;
        }
        role=roleRepository.save(role);
        response.setMessage("Success");
        response.setResult(role);
        return response;
    }


    @PutMapping("{id}")
    BaseResponse updateRole(@PathVariable Long id, @RequestBody Role role) {
        BaseResponse response = new BaseResponse();
        response.setStatus(true);
        response.setDateTime(LocalDateTime.now());

        Optional<Role> optionalRole = roleRepository.findById(id);
        if (optionalRole.isPresent()) {
            Role r = optionalRole.get();
            r.setRoleId(role.getRoleId());
            r.setName(r.getName());
            response.setResult(r);

        } else {
            role = roleRepository.save(role);
            response.setResult(role);
        }
        response.setMessage("Success");
        return response;
    }

    @DeleteMapping("{id}")
    BaseResponse deleteRole(@PathVariable Long id) {
        BaseResponse response = new BaseResponse();

        response.setDateTime(LocalDateTime.now());

        Optional<Role> optionalRole = roleRepository.findById(id);
        if (optionalRole.isPresent()) {
            response.setStatus(true);
            Role role = optionalRole.get();
            roleRepository.deleteById(id);
            response.setResult(role);
            response.setMessage("Success");
        } else {
            response.setStatus(false);
            response.setResult("There is no data with that ID.");
        }
        return response;
    }

}
