package com.thn.onlinecoursemanagement.controller;

import com.thn.onlinecoursemanagement.config.AppConfig;
import com.thn.onlinecoursemanagement.constants.Util;
import com.thn.onlinecoursemanagement.entities.Company;
import com.thn.onlinecoursemanagement.payload.response.BaseResponse;
import com.thn.onlinecoursemanagement.payload.response.CompanyResponse;
import com.thn.onlinecoursemanagement.repositories.CompanyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


/**
 * @author ThwetHmueNyein
 * @Date 02/05/2022
 */

@RestController
@RequestMapping("company")
@Slf4j
public class CompanyController {
    final CompanyRepository companyRepository;
    final AppConfig appConfig;
    final Util util;

    public CompanyController(CompanyRepository companyRepository, AppConfig appConfig, Util util) {
        this.companyRepository = companyRepository;
        this.appConfig = appConfig;
        this.util = util;
    }


    @PostMapping()
    @Secured("ROLE_ADMIN")
    @CrossOrigin
    BaseResponse createCompany(@RequestBody Company company) {
        BaseResponse response = new BaseResponse();
        response.setDateTime(LocalDateTime.now());
        if (company == null) {
            response.setStatus(false);
            response.setMessage("Fail to upload");
            return response;
        }
        try {
            company = companyRepository.save(new Company(company.getName(), company.getAddress(), LocalDateTime.now(), null));
            response.setStatus(true);
            response.setMessage("Successfully upload!");
            response.setResult(company);

        } catch (Exception e) {
            response.setStatus(false);
            response.setMessage("Fail to upload");
            log.info("Exception : ", e);
        }
        return response;
    }

    @PostMapping("/upload/{id}")
    @Secured("ROLE_ADMIN")
    @CrossOrigin
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
            String [] imageName= Objects.requireNonNull(file.getOriginalFilename()).split("\\.");

            Path path = Paths.get(appConfig.getUploadFolder() +imageName[0] +"_"+ DateTimeFormatter.ofPattern("ddMMyyyy_hhmmss").format(LocalDateTime.now())+"."+ imageName[1]);
            log.info("Image Path : {}", path);
            Files.write(path, bytes);
            Optional<Company> optionalCompany = companyRepository.findById(id);
            if (optionalCompany.isPresent()) {
                Company c = optionalCompany.get();
                c.setImageUrl(path.toString());
                companyRepository.save(c);
                response.setResult(c);
            }
            response.setStatus(true);
            response.setMessage("File upload successful!!");
            log.info("Response : {}", response);

        } catch (Exception e) {
            response.setMessage("File upload Fail!!");
            response.setStatus(false);
            log.info("Exception : ", e);

        }
        return response;
    }

    @PutMapping("{id}")
    @Secured("ROLE_ADMIN")
    @CrossOrigin
    BaseResponse updateCompany(@PathVariable Long id, @RequestBody Company company) {
        BaseResponse response = new BaseResponse();
        response.setDateTime(LocalDateTime.now());
        if (id == null) {
            response.setStatus(false);
            response.setMessage("No Path id");
            return response;
        }
        if (company == null) {
            response.setStatus(false);
            response.setMessage("No request body");
            return response;
        }
        try {
            Optional<Company> optionalCompany = companyRepository.findById(id);
            if (optionalCompany.isPresent()) {
                Company c = optionalCompany.get();
                c.setName(company.getName());
                c.setAddress(company.getAddress());
                c.setImageUrl(company.getImageUrl());
                c.setCreatedAt(c.getCreatedAt());
                companyRepository.save(c);
                response.setResult(c);
                response.setStatus(true);
                response.setMessage("Successfully Updated");
                return response;
            } else {
                response.setStatus(false);
                response.setMessage("Fail to update");
                return response;
            }

        } catch (Exception e) {
            response.setStatus(false);
            response.setMessage("Fail to update");
            log.info("Exception : ", e);
            return response;

        }

    }

    @DeleteMapping("{id}")
    @Secured("ROLE_ADMIN")
    @CrossOrigin
    BaseResponse deleteCompany(@PathVariable Long id) {
        BaseResponse response = new BaseResponse();
        response.setDateTime(LocalDateTime.now());
        try {
            Optional<Company> optionalCompany = companyRepository.findById(id);
            if (optionalCompany.isPresent()) {
                Company company = optionalCompany.get();
                companyRepository.deleteById(id);
                response.setStatus(true);
                response.setMessage("Successfully Deleted");
                response.setResult(company);
            } else {
                response.setStatus(false);
                response.setMessage("Fail to delete");
            }
        } catch (Exception e) {
            response.setStatus(false);
            response.setMessage("Fail to delete");
            log.info("Exception : ", e);
        }
        return response;

    }

    @GetMapping()
    @CrossOrigin
    BaseResponse getAllCompanies() {
        List<CompanyResponse> companyList = new ArrayList<>();
        BaseResponse response = new BaseResponse();
        response.setStatus(true);
        try {
            for (Company company : companyRepository.findAll()) {

                CompanyResponse companyResponse = new CompanyResponse(company.getId(), company.getName(), company.getCreatedAt(),
                        company.getImageUrl() == null ? null : util.encodeFileToBase64Binary(company.getImageUrl()), company.getAddress());
                companyList.add(companyResponse);
            }
            response.setResult(companyList);
            response.setMessage("Success");
        } catch (Exception e) {
            response.setMessage("Fail to delete");
            log.info("Exception : ", e);
        }
        return response;

    }
}
