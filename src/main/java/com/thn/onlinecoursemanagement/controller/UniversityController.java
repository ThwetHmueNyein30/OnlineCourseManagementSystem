package com.thn.onlinecoursemanagement.controller;

import com.thn.onlinecoursemanagement.config.AppConfig;
import com.thn.onlinecoursemanagement.constants.Util;
import com.thn.onlinecoursemanagement.entities.University;
import com.thn.onlinecoursemanagement.payload.response.BaseResponse;
import com.thn.onlinecoursemanagement.payload.response.UniversityResponse;
import com.thn.onlinecoursemanagement.repositories.UniversityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * @author ThwetHmueNyein
 * @Date 02/05/2022
 */

@Slf4j
@RestController
@RequestMapping("university")
public class UniversityController {
    final UniversityRepository universityRepository;
    final AppConfig appConfig;
    final Util util;

    public UniversityController(UniversityRepository universityRepository, AppConfig appConfig, Util util) {
        this.universityRepository = universityRepository;
        this.appConfig = appConfig;
        this.util = util;
    }

    @PostMapping()
    @CrossOrigin
    @Secured("ROLE_ADMIN")
    BaseResponse createUniversity(@RequestBody University university) {
        if (university == null) {
            return new BaseResponse(false, null,LocalDateTime.now(),"Fail to create!!");
        }
        try {
            university = universityRepository.save(new University(university.getName(), university.getAddress(), LocalDateTime.now(), null));
            return new BaseResponse(true, university,LocalDateTime.now(),"Success!!");
        } catch (Exception e) {
            return new BaseResponse(false, null,LocalDateTime.now(),"Fail!!");
        }
    }

    @PostMapping("/upload/{id}")
    @CrossOrigin
    @Secured("ROLE_ADMIN")
    BaseResponse uploadImage(@PathVariable Long id, @RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            return new BaseResponse(false, null,LocalDateTime.now(),"Fail to upload!!");
        }
        try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(appConfig.getUploadFolder() + file.getOriginalFilename());
            Files.write(path, bytes);
            Optional<University> optionalUniversity = universityRepository.findById(id);
            if (optionalUniversity.isPresent()) {
                University university = optionalUniversity.get();
                university.setImageUrl(path.toString());
                universityRepository.save(university);
                return new BaseResponse(true, university,LocalDateTime.now(),"Successfully upload!");
            } else {
                return new BaseResponse(false, null,LocalDateTime.now(),"Fail to upload");
            }
        } catch (Exception e) {
            return new BaseResponse(false, null,LocalDateTime.now(),"Fail to upload");
        }
    }


    @PutMapping("{id}")
    @CrossOrigin
    @Secured("ROLE_ADMIN")
    BaseResponse updateUniversity(@PathVariable Long id, @RequestBody University university) {
        if (university == null) {
            return new BaseResponse(false, null,LocalDateTime.now(),"No request body");
        }
        try {
            Optional<University> optionalUniversity = universityRepository.findById(id);
            if (optionalUniversity.isPresent()) {
                University u = optionalUniversity.get();
                u.setName(university.getName());
                u.setAddress(university.getAddress());
                u.setCreatedAt(u.getCreatedAt());
                u = universityRepository.save(u);
                return new BaseResponse(true, u,LocalDateTime.now(),"Successfully update");
            } else {
                return new BaseResponse(false, null,LocalDateTime.now(),"No university with that ID");
            }
        } catch (Exception e) {
            return new BaseResponse(false, null,LocalDateTime.now(),"Fail to update");
        }
    }

    @DeleteMapping("{id}")
    @CrossOrigin
    @Secured("ROLE_ADMIN")
    BaseResponse deleteUniversity(@PathVariable Long id) {
        try {
            Optional<University> optionalUniversity = universityRepository.findById(id);
            if (optionalUniversity.isPresent()) {
                University u = optionalUniversity.get();
                universityRepository.deleteById(id);
                return new BaseResponse(true, u,LocalDateTime.now(),"Successfully delete");
            } else {
                return new BaseResponse(false, null,LocalDateTime.now(),"No university with that ID");
            }
        } catch (Exception e) {
            return new BaseResponse(false, null,LocalDateTime.now(),"Fail to update");
        }
    }


    @GetMapping()
    @CrossOrigin
    BaseResponse getAllUniversities() {
        try {
            List<UniversityResponse> universityResponseList= universityRepository.findAll().stream().map(this::convertFromUniversity).collect(Collectors.toList());
            return new BaseResponse(true,universityResponseList,LocalDateTime.now(),"Success!!");
        } catch (Exception e) {
            return new BaseResponse(false,null,LocalDateTime.now(),"Fail!!");

        }
    }

    UniversityResponse convertFromUniversity(University university){
        return new UniversityResponse(
                university.getId(),
                university.getName(),
                university.getCreatedAt(),
                university.getImageUrl() == null ? null : util.encodeFileToBase64Binary(university.getImageUrl()),
                university.getAddress()
        );
    }
}
