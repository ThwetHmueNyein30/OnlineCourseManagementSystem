package com.thn.onlinecoursemanagement.services.imp;

import com.thn.onlinecoursemanagement.config.AppConfig;
import com.thn.onlinecoursemanagement.constants.RoleEnum;
import com.thn.onlinecoursemanagement.constants.Util;
import com.thn.onlinecoursemanagement.entities.Person;
import com.thn.onlinecoursemanagement.ewallet_database.entities.EWalletInfo;
import com.thn.onlinecoursemanagement.ewallet_database.services.EWalletInfoService;
import com.thn.onlinecoursemanagement.payload.request.PersonEWalletRequestBody;
import com.thn.onlinecoursemanagement.payload.response.BaseResponse;
import com.thn.onlinecoursemanagement.payload.response.PersonResponse;
import com.thn.onlinecoursemanagement.repositories.PersonRepository;
import com.thn.onlinecoursemanagement.services.KeycloakService;
import com.thn.onlinecoursemanagement.services.PersonService;
import com.thn.onlinecoursemanagement.services.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author ThwetHmueNyein
 * @Date 26/05/2022
 */
@Component
@Slf4j
public class PersonServiceImpl implements PersonService {

    final PersonRepository personRepository;
    final AppConfig appConfig;
    final RoleService roleService;
    final EWalletInfoService eWalletInfoService;
    final KeycloakService keycloakService;
    final Util util;

    public PersonServiceImpl(PersonRepository personRepository, AppConfig appConfig, RoleService roleService, EWalletInfoService eWalletInfoService, KeycloakService keycloakService, Util util) {
        this.personRepository = personRepository;
        this.appConfig = appConfig;
        this.roleService = roleService;
        this.eWalletInfoService = eWalletInfoService;
        this.keycloakService = keycloakService;
        this.util = util;
    }

    @Override
    public BaseResponse createPerson(PersonEWalletRequestBody requestBody) {
        if (requestBody == null) {
            return new BaseResponse(false,null, LocalDateTime.now(),"No Person Data");
        }
        Person person=requestBody.getPerson();
        Double balance=requestBody.getBalance();
        try {
            UserRepresentation user = keycloakService.createUser(person);
            log.info("User : {} ", user);
            if (user != null) {
                person = personRepository.save(new Person(person.getName(), person.getAddress(), LocalDateTime.now(),
                        person.getBirthDay(), person.getRoleId(),
                        person.getUniversityId(), person.getCompanyId(), person.getStatus(), null, person.getPhone(), person.getEmail(), user.getId()));
                EWalletInfo eWalletInfo=new EWalletInfo(person.getId(),LocalDateTime.now(),balance,person.getName());
                int id = eWalletInfoService.insertEWalletIfo(eWalletInfo);
                if (id == -1 ){
                    return new BaseResponse(false,null,LocalDateTime.now(),"Fail to create Data");
                }
                return new BaseResponse(true,person,LocalDateTime.now(),"Success");
            } else {
                return new BaseResponse(false,null,LocalDateTime.now(),"Fail to create user in Keycloak");
            }
        } catch (Exception e) {
            log.info("Exception in creation : ", e);
            return new BaseResponse(false,null,LocalDateTime.now(),"Fail");
        }
    }

    @Override
    public BaseResponse deletePerson(Long id) {
        try {
            Optional<Person> optionalPerson = personRepository.findById(id);
            if (optionalPerson.isPresent()) {
                Person p = optionalPerson.get();
                personRepository.deleteById(id);
                return new BaseResponse(true,p,LocalDateTime.now(),"Successfully deleted");
            } else {
                return new BaseResponse(false,null,LocalDateTime.now(),"No course with that ID");
            }
        } catch (Exception e) {
            return new BaseResponse(false,null,LocalDateTime.now(),"Fail to delete");
        }
    }


    @Override
    public BaseResponse uploadImage(Long id, MultipartFile file) {
        if (file.isEmpty()) {
            return new BaseResponse(false,null,LocalDateTime.now(),"No file found");
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
                return new BaseResponse(true,person,LocalDateTime.now(),"Successful");
            } else {
                return new BaseResponse(false,null,LocalDateTime.now(),"Fail to upload");
            }
        } catch (Exception e) {
            return new BaseResponse(false,null,LocalDateTime.now(),"Fail to upload");
        }
    }

    @Override
    public BaseResponse updatePerson(Long id, Person person) {

        if(person == null){
            return new BaseResponse(false,null,LocalDateTime.now(),"Fail to update for that id");
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
                return new BaseResponse(true,p,LocalDateTime.now(),"Successfully updated");
            } else {
                return new BaseResponse(false,null,LocalDateTime.now(),"Fail to update for that id");
            }
        } catch (Exception e) {
            return new BaseResponse(false,null,LocalDateTime.now(),"Fail to update");
        }
    }

    @Override
    public BaseResponse getAllPerson(String role) {
        String roleUpperCase = role.toUpperCase(Locale.ROOT);
        List<Person> personList=new ArrayList<>();
        if (!RoleEnum.isValidRole(roleUpperCase)) {
            return new BaseResponse(false, null, LocalDateTime.now(), "Invalid role");
        }
        if (role.equals(RoleEnum.ADMIN.getCode())) {
            personList.addAll (personRepository.findAll());
        } else {
            personList.addAll (roleService.findAllByRole(role));
        }
        List<PersonResponse> personResponseList = personList.stream().map(this::convertFromPerson).collect(Collectors.toList());
        return new BaseResponse(true, personResponseList, LocalDateTime.now(), "Successfully");
    }

    @Override
    public BaseResponse getPersonByName(String userName) {
        Person person = personRepository.findByName(userName);
        PersonResponse personResponse=convertFromPerson(person);
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
