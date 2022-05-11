package com.thn.onlinecoursemanagement.controller;

import com.thn.onlinecoursemanagement.payload.response.BaseResponse;
import com.thn.onlinecoursemanagement.payload.resquest.CourseRegisterRequestBody;
import com.thn.onlinecoursemanagement.services.CourseRegistrationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author ThwetHmueNyein
 * @Date 02/05/2022
 */


@RestController
@RequestMapping("courseRegistration")
@Slf4j
public class CourseRegistrationController {

    @Autowired
    CourseRegistrationService courseRegistrationService;

    @PostMapping()
    @CrossOrigin
    BaseResponse registerCourse(@RequestBody CourseRegisterRequestBody courseRegisterRequestBody) {
        return courseRegistrationService.validateRegistration(courseRegisterRequestBody);
    }

    @GetMapping()
    @CrossOrigin
    BaseResponse getAllCourseRegistration() {
        return courseRegistrationService.getAllCourseRegistrationList();
    }

}
