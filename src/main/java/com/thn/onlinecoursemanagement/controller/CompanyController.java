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
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


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
            return new BaseResponse(false, null, LocalDateTime.now(), "Fail to upload");
        }
        try {
            company = companyRepository.save(new Company(company.getName(), company.getAddress(), LocalDateTime.now(), null));
            return new BaseResponse(true, company, LocalDateTime.now(), "Successful!!");
        } catch (Exception e) {
            return new BaseResponse(false, null, LocalDateTime.now(), "Fail to upload");
        }
    }

    @PostMapping("/upload/{id}")
    @Secured("ROLE_ADMIN")
    @CrossOrigin
    BaseResponse uploadImage(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        log.info("Response : {} ", file);
        if (file.isEmpty()) {
            return new BaseResponse(false, null, LocalDateTime.now(), "No file found");
        }
        try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(appConfig.getUploadFolder() + file.getOriginalFilename());
            Files.write(path, bytes);
            Optional<Company> optionalCompany = companyRepository.findById(id);
            if (optionalCompany.isPresent()) {
                Company c = optionalCompany.get();
                c.setImageUrl(path.toString());
                companyRepository.save(c);
                return new BaseResponse(true, c, LocalDateTime.now(), "File upload successful!!");
            } else {
                return new BaseResponse(false, null, LocalDateTime.now(), "No company with that ID");
            }
        } catch (Exception e) {
            log.info("Exception : ",e);
            return new BaseResponse(false, null, LocalDateTime.now(), "File upload Fail!!");
        }
    }

    @PutMapping("{id}")
    @Secured("ROLE_ADMIN")
    @CrossOrigin
    BaseResponse updateCompany(@PathVariable Long id, @RequestBody Company company) {
        if (id == null) {
            return new BaseResponse(false, null, LocalDateTime.now(), "No ID found");
        }
        if (company == null) {
            return new BaseResponse(false, null, LocalDateTime.now(), "No request body");
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
                return new BaseResponse(true, c, LocalDateTime.now(), "Successfully Updated");
            } else {
                return new BaseResponse(false, null, LocalDateTime.now(), "No data with that ID");
            }

        } catch (Exception e) {
            return new BaseResponse(false, null, LocalDateTime.now(), "Fail to update");
        }

    }

    @DeleteMapping("{id}")
    @Secured("ROLE_ADMIN")
    @CrossOrigin
    BaseResponse deleteCompany(@PathVariable Long id) {
        try {
            Optional<Company> optionalCompany = companyRepository.findById(id);
            if (optionalCompany.isPresent()) {
                Company company = optionalCompany.get();
                companyRepository.deleteById(id);
                return new BaseResponse(true, company, LocalDateTime.now(), "Successful");
            } else {
                return new BaseResponse(false, null, LocalDateTime.now(), "No company exist with that Id");
            }
        } catch (Exception e) {
            return new BaseResponse(false, null, LocalDateTime.now(), "Fail to delete");
        }
    }

    @GetMapping()
    @CrossOrigin
    @Secured("ROLE_ADMIN")
    BaseResponse getAllCompanies() {
        List<Company> companyList = companyRepository.findAll();
        List<CompanyResponse> companyResponseList = companyList.stream().map(this::convertFromCompany).collect(Collectors.toList());
        return new BaseResponse(true, companyResponseList, LocalDateTime.now(), "Successful");
    }

    CompanyResponse convertFromCompany(Company company) {
        return new CompanyResponse(
                company.getId(),
                company.getName(),
                company.getCreatedAt(),
                company.getImageUrl() == null ? null : util.encodeFileToBase64Binary(company.getImageUrl()),
                company.getAddress());
    }
}
