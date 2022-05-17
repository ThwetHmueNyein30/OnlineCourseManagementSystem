package com.thn.onlinecoursemanagement.controller;

import com.thn.onlinecoursemanagement.config.AppConfig;
import com.thn.onlinecoursemanagement.entities.Person;
import com.thn.onlinecoursemanagement.payload.response.BaseResponse;
import com.thn.onlinecoursemanagement.payload.response.PersonResponse;
import com.thn.onlinecoursemanagement.repositories.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.thn.onlinecoursemanagement.util.Util.encodeFileToBase64Binary;

/**
 * @author ThwetHmueNyein
 * @Date 02/05/2022
 */

@Slf4j
@RestController
@RequestMapping("person")
public class PersonController {
    final PersonRepository personRepository;
    final AppConfig appConfig;

    public PersonController(PersonRepository personRepository, AppConfig appConfig) {
        this.personRepository = personRepository;
        this.appConfig = appConfig;
    }

    @PostMapping()
    @CrossOrigin
    @Secured("ROLE_ADMIN")
    BaseResponse createPerson(@RequestBody Person person) {
        BaseResponse response = new BaseResponse();
        response.setDateTime(LocalDateTime.now());
        if (person == null) {
            response.setStatus(false);
            response.setMessage("No Person Data");
            return response;
        }
        try {
            person = personRepository.save(new Person(person.getName(), person.getAddress(), LocalDateTime.now(),
                    person.getBirthDay(), person.getRoleId(),
                    person.getUniversityId(), person.getCompanyId(), person.getStatus(), null, person.getPhone(), person.getEmail()));
            response.setResult(person);
            response.setStatus(true);
            response.setMessage("Success");
        } catch (Exception e) {
            response.setStatus(false);
            response.setMessage("Failure");
            log.info("Exception : ", e);

        }
        return response;
    }

    @PostMapping("/upload/{id}")
    @CrossOrigin
    @Secured("ROLE_ADMIN")
    BaseResponse uploadImage(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        BaseResponse response = new BaseResponse();
        if (file.isEmpty()) {
            response.setStatus(false);
            response.setMessage("No file found");
            return response;
        }
        try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(appConfig.getUploadFolder() + file.getOriginalFilename());
            Files.write(path, bytes);
            Optional<Person> optionalPerson = personRepository.findById(id);
            if (optionalPerson.isPresent()) {
                Person person = optionalPerson.get();
                person.setImageUrl(path.toString());
                personRepository.save(person);
                response.setStatus(true);
                response.setResult(person);
                response.setMessage("File upload successful!!");
            } else {
                response.setStatus(false);
                response.setMessage("File upload Fail!!");
            }
        } catch (Exception e) {
            log.info("Exception : ", e);
            response.setStatus(false);
            response.setMessage("File upload Fail!!");
        }
        return response;

    }

    @PutMapping("{id}")
    @CrossOrigin
    @Secured("ROLE_ADMIN")
    BaseResponse updatePerson(@PathVariable Long id, @RequestBody Person person) {
        BaseResponse response = new BaseResponse();
        response.setDateTime(LocalDateTime.now());
        if (person == null) {
            response.setStatus(false);
            response.setMessage("No Request Body");
            return response;
        }
        try {
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
                response.setStatus(true);
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
    BaseResponse deleteCourse(@PathVariable Long id) {
        BaseResponse response = new BaseResponse();
        try {
            Optional<Person> optionalPerson = personRepository.findById(id);
            if (optionalPerson.isPresent()) {
                Person p = optionalPerson.get();
                personRepository.deleteById(id);
                response.setResult(p);
                response.setStatus(true);
                response.setMessage("Success");
            } else {
                response.setStatus(false);
                response.setResult("There is no data with that ID.");
            }
        } catch (Exception e) {
            response.setStatus(false);
            response.setResult("There is no data with that ID.");
            log.error("Exception : " + e);
        }
        return response;
    }

    @GetMapping()
    @CrossOrigin
    @Secured("ROLE_ADMIN")
    BaseResponse getAllPerson() {
        List<PersonResponse> personResponseList = new ArrayList<>();
        BaseResponse response = new BaseResponse();
        response.setDateTime(LocalDateTime.now());
        try {
            response.setStatus(true);
            for (Person person : personRepository.findAll()) {
                PersonResponse personResponse = new PersonResponse(
                        person.getId(),
                        person.getName(),
                        person.getCreatedAt(),
                        person.getImageUrl() == null ? null : encodeFileToBase64Binary(person.getImageUrl()),
                        person.getBirthDay(),
                        person.getRoleId(), person.getUniversityId(),
                        person.getCompanyId(), person.getStatus(), person.getPhone(), person.getEmail()
                );
                personResponseList.add(personResponse);
            }
            response.setResult(personResponseList);
            response.setMessage("Success");
        } catch (Exception e) {
            log.info("Exception : ", e);
            response.setMessage("Failure");
            response.setStatus(false);
        }
        return response;
    }

}
