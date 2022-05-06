package com.thn.onlinecoursemanagement.controller;

import com.thn.onlinecoursemanagement.entities.Course;
import com.thn.onlinecoursemanagement.payload.response.BaseResponse;
import com.thn.onlinecoursemanagement.repositories.CourseRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author ThwetHmueNyein
 * @Date 02/05/2022
 */

@RestController
@RequestMapping("course")
public class CourseController {
    final CourseRepository courseRepository;
    private static String UPLOAD_FOLDER = "/Users/mobile-5/Desktop/";


    public CourseController(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @PostMapping()
    BaseResponse createCourse(@RequestBody Course course) {
        BaseResponse response = new BaseResponse();
        response.setStatus(true);
        response.setDateTime(LocalDateTime.now());
        if(course == null){
           response.setMessage("No course");
        }
        try {
            course =courseRepository.save(new Course(course.getName(),
                    course.getContent(),
                    course.getDocumentPath(),
                    course.getFee(),
                    LocalDateTime.now(),
                    course.getRegisteredFrom(),
                    course.getRegisteredTo(),
                    course.getStatus(),
                    course.getTeacherId(),
                    course.getImageUrl()));
            response.setResult(course);
            response.setMessage("Successfully upload!");
        }
        catch (Exception e){
            response.setMessage("Error");
        }
        return response;
    }

    @PutMapping("/upload/{id}")
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
            Optional<Course> optionalCourse=courseRepository.findById(id);
            if(optionalCourse.isPresent()){
                Course c=optionalCourse.get();
                c.setImageUrl(path.toString());
                courseRepository.save(c);
                response.setResult(c);
            }
            response.setMessage("File upload successful!!");
            return response;
        } catch (Exception e) {
            response.setMessage("File upload Fail!!");
            return response;
        }
    }


    @PutMapping("{id}")
    BaseResponse updateCourse(@PathVariable Long id, @RequestBody Course course) {
        BaseResponse response = new BaseResponse();
        response.setStatus(true);
        response.setDateTime(LocalDateTime.now());
        Optional<Course> optionalCourse = courseRepository.findById(id);
        if (optionalCourse.isPresent()) {
            Course c = optionalCourse.get();
            c.setName(course.getName());
            c.setContent(course.getContent());
            c.setDocumentPath(course.getDocumentPath());
            c.setFee(course.getFee());
            c.setCreatedAt(course.getCreatedAt());
            c.setRegisteredTo(course.getRegisteredTo());
            c.setRegisteredFrom(course.getRegisteredFrom());
            c.setStatus(course.getStatus());
            c.setTeacherId(course.getTeacherId());
            c.setImageUrl(course.getImageUrl());
            courseRepository.save(c);
            response.setResult(c);
        } else {
            course = courseRepository.save(course);
            response.setResult(course);
        }
        response.setMessage("Success");
        return response;
    }

    @DeleteMapping("{id}")
    BaseResponse deleteCourse(@PathVariable Long id){
        BaseResponse response = new BaseResponse();
        response.setStatus(true);
        response.setDateTime(LocalDateTime.now());

        Optional<Course> optionalCourse=courseRepository.findById(id);
        if(optionalCourse.isPresent()){
            Course c=optionalCourse.get();
            courseRepository.deleteById(id);
            response.setMessage("Success");
            response.setResult(c);
        }else{
            response.setMessage("There is no data with that ID.");
        }
        return response;
    }


    @GetMapping()
    BaseResponse getALlCourses() {
        BaseResponse response = new BaseResponse();
        response.setStatus(true);
        response.setDateTime(LocalDateTime.now());
        response.setResult(courseRepository.findAll());
        return response;
    }


}
