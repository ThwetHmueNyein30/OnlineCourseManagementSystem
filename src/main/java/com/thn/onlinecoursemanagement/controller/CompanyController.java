package com.thn.onlinecoursemanagement.controller;

import com.thn.onlinecoursemanagement.payload.response.CompanyResponse;
import com.thn.onlinecoursemanagement.payload.response.BaseResponse;
import com.thn.onlinecoursemanagement.entities.Company;
import com.thn.onlinecoursemanagement.repositories.CompanyRepository;
import lombok.extern.slf4j.Slf4j;
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

@RestController
@RequestMapping("company")
@Slf4j
public class CompanyController {
    final CompanyRepository companyRepository;

    public CompanyController(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }
    private static String UPLOAD_FOLDER = "/Users/mobile-5/Downloads/SpringBoot/OnlineCourseManagement/src/main/upload/";

    @PostMapping()
    BaseResponse registerCompany(@RequestBody Company company) {
        BaseResponse response = new BaseResponse();
        response.setStatus(true);
        response.setDateTime(LocalDateTime.now());
        if(company == null){
            response.setMessage("No Request Body!");
            return response;
        }
        try{
            company=companyRepository.save(new Company(company.getName(), company.getAddress(), LocalDateTime.now(), company.getImageUrl()));
            response.setMessage("Successfully upload!");
            response.setResult(company);
        }catch (Exception e){
            log.info("Exception : ", e);
            response.setMessage("Error");
        }
        return response;
    }

    @PostMapping("/upload/{id}")
    BaseResponse handleFileUpload(@PathVariable Long id, @RequestParam("file") MultipartFile file ) {
        BaseResponse response = new BaseResponse();
        response.setStatus(true);
        response.setDateTime(LocalDateTime.now());

        if (file.isEmpty()) {
            response.setMessage("No file found");
            return response;
        }
        try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOAD_FOLDER + file.getOriginalFilename());
            Files.write(path, bytes);
            Optional<Company> optionalCompany=companyRepository.findById(id);
            if(optionalCompany.isPresent()){
                Company c=optionalCompany.get();
                c.setImageUrl(path.toString());
                companyRepository.save(c);
                response.setResult(c);
            }
            response.setMessage("File upload successful!!");
            log.info("Response : {}", response);
            return response;
        } catch (Exception e) {
            response.setMessage("File upload Fail!!");
            return response;
        }
    }


    @PutMapping("{id}")
    BaseResponse updateCompany(@PathVariable Long id, @RequestBody Company company) {
        BaseResponse response = new BaseResponse();
        response.setStatus(true);
        response.setDateTime(LocalDateTime.now());

        Optional<Company> optionalCompany = companyRepository.findById(id);
        if (optionalCompany.isPresent()) {
            Company c = optionalCompany.get();
            c.setName(company.getName());
            c.setAddress(company.getAddress());
            c.setCreatedAt(company.getCreatedAt());
            c.setImageUrl(company.getImageUrl());
            companyRepository.save(c);
            response.setResult(c);
        } else {
            company = companyRepository.save(company);
            response.setResult(company);
        }
        response.setMessage("Success");
        return response;
    }

    @DeleteMapping("{id}")
    BaseResponse deleteCompany(@PathVariable Long id){
        BaseResponse response = new BaseResponse();
        response.setStatus(true);
        response.setDateTime(LocalDateTime.now());

        Optional<Company> optionalCompany=companyRepository.findById(id);
        if(optionalCompany.isPresent()){
            Company company=optionalCompany.get();
            companyRepository.deleteById(id);
            response.setResult(company);
        }else{
            response.setResult("There is no data with that ID.");
        }
        response.setMessage("Success");
        return response;
    }

    @GetMapping()
    BaseResponse getAllCompanies() {
        List<CompanyResponse> companyList=new ArrayList<>();
        BaseResponse response = new BaseResponse();
        response.setStatus(true);
        response.setDateTime(LocalDateTime.now());
        for(Company company: companyRepository.findAll()){
            CompanyResponse companyResponse = new CompanyResponse(company.getId(), company.getName(),
                    company.getAddress(), company.getCreatedAt(), company.getImageUrl() == null ? null : company.getImageUrl().getBytes());

            companyList.add(companyResponse);
        }
        response.setResult(companyList);

        return response;
    }
}
