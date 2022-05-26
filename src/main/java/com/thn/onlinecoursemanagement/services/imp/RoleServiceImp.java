package com.thn.onlinecoursemanagement.services.imp;

import com.thn.onlinecoursemanagement.entities.Person;
import com.thn.onlinecoursemanagement.entities.Role;
import com.thn.onlinecoursemanagement.repositories.PersonRepository;
import com.thn.onlinecoursemanagement.repositories.RoleRepository;
import com.thn.onlinecoursemanagement.services.RoleService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * @author ThwetHmueNyein
 * @Date 19/05/2022
 */
@Component
public class RoleServiceImp implements RoleService {
    final PersonRepository personRepository;
    final RoleRepository roleRepository;

    public RoleServiceImp(PersonRepository personRepository, RoleRepository roleRepository) {
        this.personRepository = personRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public List<Person> findAllByRole(String role) {
        return personRepository.findAllByRole(role);
    }

    @Override
    public String finRoleName(Long roleId) {
        Optional<Role> roleOptional = roleRepository.findByRoleId(roleId);
        if (roleOptional.isPresent()) return roleOptional.get().getName();
        else return "";
    }
}
