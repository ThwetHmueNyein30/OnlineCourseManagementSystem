package com.thn.onlinecoursemanagement.controller;

import com.thn.onlinecoursemanagement.config.AppConfig;
import com.thn.onlinecoursemanagement.constants.RoleEnum;
import com.thn.onlinecoursemanagement.constants.Util;
import com.thn.onlinecoursemanagement.entities.Person;
import com.thn.onlinecoursemanagement.ewallet_database.entities.EWalletInfo;
import com.thn.onlinecoursemanagement.ewallet_database.repositories.EWalletInfoService;
import com.thn.onlinecoursemanagement.payload.request.PersonEWalletRequestBody;
import com.thn.onlinecoursemanagement.payload.response.BaseResponse;
import com.thn.onlinecoursemanagement.payload.response.PersonResponse;
import com.thn.onlinecoursemanagement.repositories.PersonRepository;
import com.thn.onlinecoursemanagement.services.KeycloakService;
import com.thn.onlinecoursemanagement.services.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;


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
    final RoleService roleService;
    final EWalletInfoService eWalletInfoService;
    final KeycloakService keycloakService;
    final Util util;

    public PersonController(PersonRepository personRepository, AppConfig appConfig, RoleService roleService, EWalletInfoService eWalletInfoService, KeycloakService keycloakService, Util util) {
        this.personRepository = personRepository;
        this.appConfig = appConfig;
        this.roleService = roleService;
        this.eWalletInfoService = eWalletInfoService;
        this.keycloakService = keycloakService;
        this.util = util;
    }

    @PostMapping()
    @CrossOrigin
    @Secured("ROLE_ADMIN")
    BaseResponse createPerson(@RequestBody PersonEWalletRequestBody requestBody) {
        BaseResponse response = new BaseResponse();
        response.setDateTime(LocalDateTime.now());
        if (requestBody == null) {
            response.setStatus(false);
            response.setMessage("No Person Data");
            return response;
        }
        Person person=requestBody.getPerson();
        Double balance=requestBody.getBalance();

        try {
            UserRepresentation user = keycloakService.createUser(person);
            if (user != null) {
                person = personRepository.save(new Person(person.getName(), person.getAddress(), LocalDateTime.now(),
                        person.getBirthDay(), person.getRoleId(),
                        person.getUniversityId(), person.getCompanyId(), person.getStatus(), null, person.getPhone(), person.getEmail(), user.getId()));
                EWalletInfo eWalletInfo=new EWalletInfo(person.getId(),LocalDateTime.now(),balance,person.getName());
                eWalletInfoService.insertEWalletIfo(eWalletInfo);
                response.setResult(person);
                response.setStatus(true);
                response.setMessage("Success");
            } else {
                response.setStatus(false);
                response.setMessage("Fail to create User in Keycloak");
            }
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
                p.setBirthDay(person.getBirthDay());
                p.setRoleId(person.getRoleId());
                p.setUniversityId(person.getUniversityId());
                p.setCompanyId(person.getCompanyId());
                p.setStatus(person.getStatus());
                p.setImageUrl(person.getImageUrl());
                p.setPhone(person.getPhone());
                p.setCreatedAt(p.getCreatedAt());
                p.setEmail(person.getEmail());
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
    BaseResponse getAllPerson(@RequestParam(value = "role", required = false) String role) {
        String roleUpperCase = role.toUpperCase(Locale.ROOT);
        List<Person> personList;
        if (!RoleEnum.isValidRole(roleUpperCase)) {
            return new BaseResponse(false, null, LocalDateTime.now(), "Invalid role");
        }
        if (roleUpperCase.equals(RoleEnum.ADMIN.getCode())) {
            personList = personRepository.findAll();
        } else {
            personList = roleService.findAllByRole(roleUpperCase);
        }
        List<PersonResponse> personResponseList = personList.stream().map(this::convertFromPerson).collect(Collectors.toList());

        return new BaseResponse(true, personResponseList, LocalDateTime.now(), "Successfully");
    }

    @GetMapping("/getByName")
    @CrossOrigin
    BaseResponse getPersonByName(@RequestParam(value = "username") String username) {
        Person person = personRepository.findByName(username);
        PersonResponse personResponse=convertFromPerson(person);
//        List<PersonResponse> personResponseList = personList.stream().map(this::convertFromPerson).collect(Collectors.toList());
        return new BaseResponse(true, personResponse, LocalDateTime.now(), "Successfully");
    }

    PersonResponse convertFromPerson(Person person) {
        return new PersonResponse(person.getId(),
                person.getName(),
                person.getCreatedAt(),
                person.getImageUrl() == null ? null : util.encodeFileToBase64Binary(person.getImageUrl()),
                person.getBirthDay(),
                person.getRoleId(), person.getUniversityId(),
                person.getCompanyId(),
                person.getStatus(),
                person.getPhone(),
                person.getEmail(),
                person.getAddress(),
                person.getKeycloakId());

    }

}
