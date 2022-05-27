package com.thn.onlinecoursemanagement.controller;

import com.thn.onlinecoursemanagement.payload.response.BaseResponse;
import com.thn.onlinecoursemanagement.services.CourseRegistrationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

/**
 * @author ThwetHmueNyein
 * @Date 02/05/2022
 */


@RestController
@RequestMapping("courseRegistration")
@Slf4j
public class CourseRegistrationController {


    private final CourseRegistrationService courseRegistrationService;

    public CourseRegistrationController(CourseRegistrationService courseRegistrationService) {
        this.courseRegistrationService = courseRegistrationService;
    }

    @PutMapping("{courseId}")
    @CrossOrigin
    @Secured({"ROLE_STUDENT"})
    BaseResponse registerCourse(@PathVariable Long courseId) {
        return courseRegistrationService.validateRegistration(courseId);
    }

    @GetMapping()
    @CrossOrigin
    @Secured({"ROLE_STUDENT", "ROLE_ADMIN"})
    BaseResponse getAllCourseRegistration() {
        return courseRegistrationService.getAllCourseRegistrationList();
    }


}
