package com.thn.onlinecoursemanagement.controller;

import com.thn.onlinecoursemanagement.payload.response.BaseResponse;
import com.thn.onlinecoursemanagement.entities.University;
import com.thn.onlinecoursemanagement.repositories.UniversityRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author ThwetHmueNyein
 * @Date 02/05/2022
 */



@RestController
@RequestMapping("university")
public class UniversityController {
    final UniversityRepository universityRepository;

    public UniversityController(UniversityRepository universityRepository) {
        this.universityRepository = universityRepository;
    }

    @PostMapping()
    BaseResponse createUniversity(@RequestBody University university) {
        BaseResponse response = new BaseResponse();
        response.setStatus(true);
        response.setDateTime(LocalDateTime.now());
        if(university==null){
            response.setMessage("No university data!");
            return response;
        }
        university=universityRepository.save(new University(university.getName(),university.getAddress(),LocalDateTime.now(),university.getImageUrl()));
        response.setResult(university);
        response.setMessage("Success");
        return response;
    }

    @PutMapping("{id}")
    BaseResponse updateUniversity(@PathVariable Long id, @RequestBody University university) {
        BaseResponse response = new BaseResponse();
        response.setStatus(true);
        response.setDateTime(LocalDateTime.now());

        Optional<University> optionalUniversity = universityRepository.findById(id);
        if (optionalUniversity.isPresent()) {
            University u = optionalUniversity.get();
            u.setName(university.getName());
            u.setAddress(university.getAddress());
            u.setCreatedAt(university.getCreatedAt());
            u.setImageUrl(university.getImageUrl());
            universityRepository.save(u);
            response.setResult(u);

        } else {
            university = universityRepository.save(university);
            response.setResult(university);
        }
        response.setMessage("Success");
        return response;
    }

    @DeleteMapping("{id}")
    BaseResponse deleteUniversity(@PathVariable Long id){
        BaseResponse response = new BaseResponse();
        response.setStatus(true);
        response.setDateTime(LocalDateTime.now());

        Optional<University> optionalUniversity=universityRepository.findById(id);
        if(optionalUniversity.isPresent()){
            University p=optionalUniversity.get();
            universityRepository.deleteById(id);
            response.setMessage("Success");
            response.setResult(p);
        }else{
            response.setResult("There is no data with that ID.");
        }
        return response;
    }


    @GetMapping()
    BaseResponse getAllUniversities() {
        BaseResponse response = new BaseResponse();
        response.setStatus(true);
        response.setDateTime(LocalDateTime.now());

        response.setResult(universityRepository.findAll());
        return response;
    }
}
