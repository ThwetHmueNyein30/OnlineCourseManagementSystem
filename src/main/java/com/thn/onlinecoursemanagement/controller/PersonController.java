package com.thn.onlinecoursemanagement.controller;

import com.thn.onlinecoursemanagement.payload.response.BaseResponse;
import com.thn.onlinecoursemanagement.entities.Person;
import com.thn.onlinecoursemanagement.repositories.PersonRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author ThwetHmueNyein
 * @Date 02/05/2022
 */


@RestController
@RequestMapping("person")
public class PersonController {
    final PersonRepository personRepository;

    public PersonController(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @PostMapping()
    BaseResponse createPerson(@RequestBody Person person) {
        BaseResponse response = new BaseResponse();
        response.setStatus(true);
        response.setDateTime(LocalDateTime.now());
        if(person==null){
            response.setMessage("No Person Data");
            return response;
        }
        person =personRepository.save(new Person(person.getName(),person.getAddress(),person.getCreatedAt(),
                person.getBirthDay(),person.getRoleId(),
                person.getUniversityId(),person.getCompanyId(),person.getStatus(),person.getImageUrl(),person.getPhone(),person.getEmail()));
        response.setResult(person);
        response.setMessage("Success");
        return response;
    }

    @PutMapping("{id}")
    BaseResponse updatePerson(@PathVariable Long id, @RequestBody Person person) {
        BaseResponse response = new BaseResponse();
        response.setStatus(true);
        response.setDateTime(LocalDateTime.now());

        Optional<Person> optionalPerson = personRepository.findById(id);
        if (optionalPerson.isPresent()) {
            Person p = optionalPerson.get();
            p.setName(person.getName());
            p.setAddress(person.getAddress());
            p.setCreatedAt(person.getCreatedAt());
            p.setBirthDay(person.getBirthDay());
            p.setRoleId(person.getRoleId());
            p.setUniversityId(person.getUniversityId());
            p.setCompanyId(person.getCompanyId());
            p.setStatus(person.getStatus());
            p.setImageUrl(person.getImageUrl());
            person.setPhone(person.getPhone());
            personRepository.save(p);
            response.setResult(p);

        } else {
            person = personRepository.save(person);
            response.setResult(person);
        }
        response.setMessage("Success");
        return response;
    }

    @DeleteMapping("{id}")
    BaseResponse deleteCourse(@PathVariable Long id){
        BaseResponse response = new BaseResponse();
        response.setStatus(true);

        Optional<Person> optionalPerson=personRepository.findById(id);
        if(optionalPerson.isPresent()){
            Person p=optionalPerson.get();
            personRepository.deleteById(id);
            response.setResult(p);
            response.setMessage("Success");
        }else{
            response.setResult("There is no data with that ID.");
        }
        return response;
    }


    @GetMapping()
    BaseResponse getALlCourses() {
        BaseResponse response = new BaseResponse();
        response.setStatus(true);
        response.setResult(personRepository.findAll());
        return response;
    }

}
