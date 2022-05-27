package com.thn.onlinecoursemanagement.controller;

import com.thn.onlinecoursemanagement.entities.Person;
import com.thn.onlinecoursemanagement.payload.request.PersonEWalletRequestBody;
import com.thn.onlinecoursemanagement.payload.response.BaseResponse;
import com.thn.onlinecoursemanagement.services.PersonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


/**
 * @author ThwetHmueNyein
 * @Date 02/05/2022
 */

@Slf4j
@RestController
@RequestMapping("person")
public class PersonController {

    final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping()
    @CrossOrigin
    @Secured("ROLE_ADMIN")
    BaseResponse createPerson(@RequestBody PersonEWalletRequestBody requestBody) {
        return personService.createPerson(requestBody);
    }

    @PostMapping("/upload/{id}")
    @CrossOrigin
    @Secured("ROLE_ADMIN")
    BaseResponse uploadImage(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
       return  personService.uploadImage(id,file);
    }

    @PutMapping("{id}")
    @CrossOrigin
    @Secured("ROLE_ADMIN")
    BaseResponse updatePerson(@PathVariable Long id, @RequestBody PersonEWalletRequestBody personEWalletRequestBody) {
        return personService.updatePerson(id,personEWalletRequestBody);
    }

    @DeleteMapping("{id}")
    @CrossOrigin
    @Secured("ROLE_ADMIN")
    BaseResponse deletePerson(@PathVariable Long id) {
       return personService.deletePerson(id);
    }

    @GetMapping()
    @CrossOrigin
    @Secured("ROLE_ADMIN")
    BaseResponse getAllPerson(@RequestParam(value = "role", required = false) String role) {
        return personService.getAllPerson(role);
    }

    @GetMapping("/getByName")
    @CrossOrigin
    BaseResponse getPersonByName(@RequestParam(value = "username") String username) {
       return personService.getPersonByName(username);
    }

}
