package com.thn.onlinecoursemanagement.controller;

import com.thn.onlinecoursemanagement.config.AppConfig;
import com.thn.onlinecoursemanagement.entities.Course;
import com.thn.onlinecoursemanagement.payload.response.BaseResponse;
import com.thn.onlinecoursemanagement.payload.response.CourseResponse;
import com.thn.onlinecoursemanagement.repositories.CourseRepository;
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

import static com.thn.onlinecoursemanagement.constants.Util.encodeFileToBase64Binary;

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


    public CourseController(CourseRepository courseRepository, AppConfig appConfig) {
        this.courseRepository = courseRepository;
        this.appConfig = appConfig;
    }

    @PostMapping()
    @CrossOrigin
    @Secured({"ROLE_TEACHER","ROLE_ADMIN"})
    BaseResponse createCourse(@RequestBody Course course) {
        BaseResponse response = new BaseResponse();
        response.setDateTime(LocalDateTime.now());
        if (course == null) {
            response.setStatus(false);
            response.setMessage("No request body");
            return response;
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
            response.setResult(course);
            response.setStatus(true);
            response.setMessage("Successfully upload!");

        } catch (Exception e) {
            response.setStatus(false);
            response.setMessage("Fail to upload");
            log.info("Exception : ", e);
        }
        return response;

    }

    @PostMapping("/upload/{id}")
    @CrossOrigin
    @Secured({"ROLE_TEACHER","ROLE_ADMIN"})
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
            Optional<Course> optionalCourse = courseRepository.findById(id);
            if (optionalCourse.isPresent()) {
                Course c = optionalCourse.get();
                c.setImageUrl(path.toString());
                courseRepository.save(c);
                response.setStatus(true);
                response.setResult(c);
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
    @Secured({"ROLE_TEACHER","ROLE_ADMIN"})
    BaseResponse updateCourse(@PathVariable Long id, @RequestBody Course course) {
        BaseResponse response = new BaseResponse();
        response.setDateTime(LocalDateTime.now());
        if (course == null) {
            response.setStatus(false);
            response.setMessage("No Request Body");
            return response;
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
                courseRepository.save(c);
                response.setResult(c);
                response.setStatus(true);
                response.setMessage("Successfully updated");
            } else {
                response.setStatus(false);
                response.setMessage("Failed to update");
            }

        } catch (Exception e) {
            log.info("Exception : ", e);
            response.setStatus(false);
            response.setMessage("File upload Fail!!");
        }
        return response;

    }

    @DeleteMapping("{id}")
    @CrossOrigin
    @Secured({"ROLE_TEACHER","ROLE_ADMIN"})
    BaseResponse deleteCourse(@PathVariable Long id) {
        BaseResponse response = new BaseResponse();
        response.setDateTime(LocalDateTime.now());
        try {
            Optional<Course> optionalCourse = courseRepository.findById(id);
            if (optionalCourse.isPresent()) {
                Course c = optionalCourse.get();
                courseRepository.deleteById(id);
                response.setStatus(true);
                response.setMessage("Successfully Deleted");
                response.setResult(c);
            } else {
                response.setStatus(false);
                response.setMessage("There is no data with that ID.");
            }
        } catch (Exception e) {
            log.info("Exception : ", e);
            response.setMessage("File to Delete");
        }
        return response;

    }

    @GetMapping()
    @CrossOrigin
    BaseResponse getALlCourses() {
        List<CourseResponse> courseResponseList = new ArrayList<>();
        BaseResponse response = new BaseResponse();
        response.setDateTime(LocalDateTime.now());
        try {
            response.setStatus(true);
            for (Course course : courseRepository.findAll()) {
                CourseResponse courseResponse=new CourseResponse(course.getId(),course.getName(),
                        course.getCreatedAt(),
                        course.getImageUrl() == null ? null :encodeFileToBase64Binary(course.getImageUrl()),
                        course.getContent(),
                        course.getFee(),
                        course.getRegisteredTo(),
                        course.getRegisteredFrom(),
                        course.getStatus(),
                        course.getTeacherId());
                courseResponseList.add(courseResponse);
            }
            response.setResult(courseResponseList);
            response.setMessage("Success");
        } catch (Exception e) {
            log.info("Exception : ", e);
            response.setMessage("Failure");
            response.setStatus(false);
        }
        return response;
    }

}
