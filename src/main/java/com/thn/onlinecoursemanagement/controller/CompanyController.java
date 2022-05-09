package com.thn.onlinecoursemanagement.controller;

import com.thn.onlinecoursemanagement.config.AppConfig;
import com.thn.onlinecoursemanagement.payload.response.BaseImageResponse;
import com.thn.onlinecoursemanagement.payload.response.CompanyResponse;
import com.thn.onlinecoursemanagement.payload.response.BaseResponse;
import com.thn.onlinecoursemanagement.entities.Company;
import com.thn.onlinecoursemanagement.repositories.CompanyRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
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
    final AppConfig appConfig;

    public CompanyController(CompanyRepository companyRepository, AppConfig appConfig) {
        this.companyRepository = companyRepository;
        this.appConfig = appConfig;
    }

    @PostMapping()
    @CrossOrigin
    BaseResponse registerCompany(@RequestBody Company company) {
        BaseResponse response = new BaseResponse();
        response.setStatus(true);
        if(company == null){
            response.setMessage("Fail to upload");
            return response;
        }
        try{
            company=companyRepository.save(new Company(company.getName(), company.getAddress(), LocalDateTime.now(), company.getImageUrl()));
            response.setMessage("Successfully upload!");
            response.setResult(company);
        }catch (Exception e){
            log.info("Exception : ", e);
            response.setMessage("Fail to upload");
        }
        return response;
    }

    @PostMapping("/upload/{id}")
    BaseResponse uploadImage(@PathVariable Long id, @RequestParam("file") MultipartFile file ) {
        BaseResponse response = new BaseResponse();
        response.setStatus(true);
        response.setDateTime(LocalDateTime.now());

        if (file.isEmpty()) {
            response.setMessage("No file found");
            return response;
        }
        try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(appConfig.getUploadFolder() + file.getOriginalFilename());
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
        if(id==null){
            response.setMessage("No Path id");
            return response;
        }
        if(company == null){
            response.setMessage("No request body");
            return response;
        }
        Optional<Company> optionalCompany = companyRepository.findById(id);
        if (optionalCompany.isPresent()) {
            Company c = optionalCompany.get();
            c.setName(company.getName());
            c.setAddress(company.getAddress());
            c.setCreatedAt(company.getCreatedAt());
            c.setImageUrl(company.getImageUrl());
            companyRepository.save(c);
            response.setResult(c);
            response.setMessage("Successfully Updated");
        } else {
            response.setMessage("Fail to update");
        }
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
            response.setMessage("Successfully Deleted");
            response.setResult(company);
        }else{
            response.setMessage("Fail to delete");
        }
        return response;
    }

    @GetMapping()
    BaseResponse getAllCompanies() throws Exception {
        List<CompanyResponse> companyList=new ArrayList<>();
        BaseResponse response = new BaseResponse();
        response.setStatus(true);
        response.setDateTime(LocalDateTime.now());
        for(Company company: companyRepository.findAll()){
           new BaseImageResponse(company.getId(),company.getName(),company.getCreatedAt(),
                    company.getImageUrl() == null ? null : encodeFileToBase64Binary(company.getImageUrl()));
            CompanyResponse companyResponse = new CompanyResponse(company.getAddress());
            companyList.add(companyResponse);
        }
        response.setResult(companyList);
        response.setMessage("Success");

        return response;
    }

    private static String encodeFileToBase64Binary(String imgUrl) throws Exception{
        File file=new File(new URL(imgUrl).toURI());
        FileInputStream fileInputStreamReader = new FileInputStream(file);
        byte[] bytes = new byte[(int)file.length()];
        fileInputStreamReader.read(bytes);
        return new String(Base64.encodeBase64(bytes), StandardCharsets.UTF_8);
    }
}
