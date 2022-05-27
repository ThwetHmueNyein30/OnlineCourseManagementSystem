package com.thn.onlinecoursemanagement.controller;

import com.thn.onlinecoursemanagement.config.AppConfig;
import com.thn.onlinecoursemanagement.constants.Util;
import com.thn.onlinecoursemanagement.entities.Company;
import com.thn.onlinecoursemanagement.entities.Course;
import com.thn.onlinecoursemanagement.payload.response.BaseResponse;
import com.thn.onlinecoursemanagement.payload.response.CompanyResponse;
import com.thn.onlinecoursemanagement.payload.response.CourseResponse;
import com.thn.onlinecoursemanagement.repositories.CourseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * @author ThwetHmueNyein
 * @Date 02/05/2022
 */

@Slf4j
@RestController
@RequestMapping("course")
public class CourseController {
    final CourseRepository courseRepository;
    final AppConfig appConfig;
    final Util util;

    public CourseController(CourseRepository courseRepository, AppConfig appConfig, Util util) {
        this.courseRepository = courseRepository;
        this.appConfig = appConfig;
        this.util = util;
    }

    @PostMapping()
    @CrossOrigin
    @Secured({"ROLE_TEACHER","ROLE_ADMIN"})
    BaseResponse createCourse(@RequestBody Course course) {
        if (course == null) {
            return new BaseResponse(false,null,LocalDateTime.now(),"No request body");
        }
        try {
            course = courseRepository.save(new Course(course.getName(),
                    course.getContent(),
                    course.getFee(),
                    LocalDateTime.now(),
                    course.getRegisteredFrom(),
                    course.getRegisteredTo(),
                    course.getStatus(),
                    course.getTeacherId(),
                    null));
            return new BaseResponse(true,course,LocalDateTime.now(),"Successfully upload");
        } catch (Exception e) {
            return new BaseResponse(false,null,LocalDateTime.now(),"Fail to create course");
        }
    }

    @PostMapping("/upload/{id}")
    @CrossOrigin
    @Secured({"ROLE_TEACHER","ROLE_ADMIN"})
    BaseResponse uploadImage(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return new BaseResponse(false,null,LocalDateTime.now(),"File is empty");
        }
        try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(appConfig.getUploadFolder() + file.getOriginalFilename());
            Files.write(path, bytes);
            Optional<Course> optionalCourse = courseRepository.findById(id);
            if (optionalCourse.isPresent()) {
                Course c = optionalCourse.get();
                c.setImageUrl(path.toString());
                courseRepository.save(c);
                return new BaseResponse(true,c,LocalDateTime.now(),"Successfully created!!!");
            } else {
                return new BaseResponse(false,null,LocalDateTime.now(),"No course with that ID.");
            }
        } catch (Exception e) {
            return new BaseResponse(false,null,LocalDateTime.now(),"Fail to upload Image");
        }
    }

    @PutMapping("{id}")
    @CrossOrigin
    @Secured({"ROLE_TEACHER","ROLE_ADMIN"})
    BaseResponse updateCourse(@PathVariable Long id, @RequestBody Course course) {
        if (course == null) {
            return new BaseResponse(false, null, LocalDateTime.now(), "No Request Body!!");
        }
        try {
            Optional<Course> optionalCourse = courseRepository.findById(id);
            if (optionalCourse.isPresent()) {
                Course c = optionalCourse.get();
                c.setName(course.getName());
                c.setContent(course.getContent());
                c.setFee(course.getFee());
                c.setRegisteredTo(course.getRegisteredTo());
                c.setRegisteredFrom(course.getRegisteredFrom());
                c.setStatus(course.getStatus());
                c.setTeacherId(course.getTeacherId());
                c.setImageUrl(course.getImageUrl());
                c.setCreatedAt(c.getCreatedAt());
                courseRepository.save(c);
                return new BaseResponse(true, c, LocalDateTime.now(), "Successfully updated");
            } else {
                return new BaseResponse(false, null, LocalDateTime.now(), "No Request Body!!");
            }
        } catch (Exception e) {
            return new BaseResponse(false, null, LocalDateTime.now(), "File upload fail");
        }
    }

    @DeleteMapping("{id}")
    @CrossOrigin
    @Secured({"ROLE_TEACHER","ROLE_ADMIN"})
    BaseResponse deleteCourse(@PathVariable Long id) {
        try {
            Optional<Course> optionalCourse = courseRepository.findById(id);
            if (optionalCourse.isPresent()) {
                Course c = optionalCourse.get();
                courseRepository.deleteById(id);
                return new BaseResponse(true, c, LocalDateTime.now(), "Successful Deleted");
            } else {
                return new BaseResponse(false, null, LocalDateTime.now(), "No Course with that ID");
            }
        } catch (Exception e) {
            return new BaseResponse(false, null, LocalDateTime.now(), "Fail to delete");
        }
    }

    @GetMapping()
    @CrossOrigin
    @Secured({"ROLE_TEACHER","ROLE_ADMIN","ROLE_STUDENT"})
    BaseResponse getALlCourses() {
        List<Course> courseList = courseRepository.findAll();
        List<CourseResponse> courseResponseList = courseList.stream().map(this::convertFromCourse).collect(Collectors.toList());
        return new BaseResponse(true, courseResponseList, LocalDateTime.now(), "Successful");
    }

    CourseResponse convertFromCourse(Course course){
        return new CourseResponse(course.getId(),course.getName(),
                course.getCreatedAt(),
                course.getImageUrl() == null ? null : util.encodeFileToBase64Binary(course.getImageUrl()),
                course.getContent(),
                course.getFee(),
                course.getRegisteredTo(),
                course.getRegisteredFrom(),
                course.getStatus(),
                course.getTeacherId());
    }

}
