package com.thn.onlinecoursemanagement.services.imp;

import com.thn.onlinecoursemanagement.entities.Person;
import com.thn.onlinecoursemanagement.repositories.PersonRepository;
import com.thn.onlinecoursemanagement.services.RoleService;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author ThwetHmueNyein
 * @Date 19/05/2022
 */
@Component
public class RoleServiceImp implements RoleService {
    final PersonRepository personRepository;

    public RoleServiceImp(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public List<Person> findAllByRole(String role) {
        return personRepository.findAllByRole(role);
    }
}
