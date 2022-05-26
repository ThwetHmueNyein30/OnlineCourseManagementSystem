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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


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
        BaseResponse response = new BaseResponse();
        response.setDateTime(LocalDateTime.now());
        if (university == null) {
            response.setStatus(false);
            response.setMessage("No university data!");
            return response;
        }
        try {
            university = universityRepository.save(new University(university.getName(), university.getAddress(), LocalDateTime.now(), null));
            response.setResult(university);
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
        response.setDateTime(LocalDateTime.now());
        if (file.isEmpty()) {
            response.setStatus(false);
            response.setMessage("No file found");
            return response;
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
                response.setStatus(true);
                response.setResult(university);
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
    BaseResponse updateUniversity(@PathVariable Long id, @RequestBody University university) {
        BaseResponse response = new BaseResponse();
        response.setDateTime(LocalDateTime.now());
        if (university == null) {
            response.setStatus(false);
            response.setMessage("No request body");
            return response;
        }
        try {
            Optional<University> optionalUniversity = universityRepository.findById(id);
            if (optionalUniversity.isPresent()) {
                University u = optionalUniversity.get();
                u.setName(university.getName());
                u.setAddress(university.getAddress());
                u.setImageUrl(university.getImageUrl());
                u.setCreatedAt(u.getCreatedAt());
                universityRepository.save(u);
                response.setResult(u);
                response.setStatus(true);
                response.setMessage("Successfully updated");
            } else {
                response.setStatus(false);
                response.setMessage("Fail to update");
            }
        } catch (Exception e) {
            response.setStatus(false);
            response.setMessage("Fail to update");
            log.info("Exception : ", e);
        }
        return response;
    }

    @DeleteMapping("{id}")
    @CrossOrigin
    @Secured("ROLE_ADMIN")
    BaseResponse deleteUniversity(@PathVariable Long id) {
        BaseResponse response = new BaseResponse();
        response.setDateTime(LocalDateTime.now());
        try {
            Optional<University> optionalUniversity = universityRepository.findById(id);
            if (optionalUniversity.isPresent()) {
                University p = optionalUniversity.get();
                universityRepository.deleteById(id);
                response.setStatus(true);
                response.setMessage("Success");
                response.setResult(p);
            } else {
                response.setStatus(false);
                response.setResult("There is no data with that ID.");
            }
        } catch (Exception e) {
            response.setStatus(false);
            response.setResult("Fail!");
            log.info("Exception : ", e);
        }
        return response;
    }


    @GetMapping()
    @CrossOrigin
    BaseResponse getAllUniversities() {
        List<UniversityResponse> universityResponseList = new ArrayList<>();
        BaseResponse response = new BaseResponse();
        response.setDateTime(LocalDateTime.now());
        try {
            response.setStatus(true);
            for (University university : universityRepository.findAll()) {
                UniversityResponse universityResponse = new UniversityResponse(
                        university.getId(),
                        university.getName(),
                        university.getCreatedAt(),
                        university.getImageUrl() == null ? null : util.encodeFileToBase64Binary(university.getImageUrl()),
                        university.getAddress()
                );
                universityResponseList.add(universityResponse);
            }
            response.setResult(universityResponseList);
            response.setMessage("Success");
        } catch (Exception e) {
            log.info("Exception : ", e);
            response.setMessage("Failure");
            response.setStatus(false);
        }
        return response;
    }
}
