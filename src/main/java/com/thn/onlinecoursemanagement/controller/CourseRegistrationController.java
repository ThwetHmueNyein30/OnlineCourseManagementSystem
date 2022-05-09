package com.thn.onlinecoursemanagement.controller;

import com.thn.onlinecoursemanagement.ewallet_database.repositories.EWalletRepository;
import com.thn.onlinecoursemanagement.entities.*;
import com.thn.onlinecoursemanagement.payload.response.BaseResponse;
import com.thn.onlinecoursemanagement.payload.resquest.CourseRegisterRequestBody;
import com.thn.onlinecoursemanagement.constant.RoleEnum;
import com.thn.onlinecoursemanagement.payload.resquest.SMSRequestBody;
import com.thn.onlinecoursemanagement.repositories.*;
import com.thn.onlinecoursemanagement.services.CourseRegistrationService;
import com.thn.onlinecoursemanagement.services.SMSService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

import static com.thn.onlinecoursemanagement.constant.Constant.SMS_ADDRESS;

/**
 * @author ThwetHmueNyein
 * @Date 02/05/2022
 */


@RestController
@RequestMapping("courseRegistration")
@Slf4j
public class CourseRegistrationController {

    @Autowired
    CourseRegistrationRepository courseRegistrationRepository;

    @Autowired
    CourseRegistrationService courseRegistrationService;


    @PostMapping()
    BaseResponse registerCourse(@RequestBody CourseRegisterRequestBody courseRegisterRequestBody) {

        return courseRegistrationService.validateRegistration(courseRegisterRequestBody);
    }


    @GetMapping()
    BaseResponse getAllCourseRegistration(){
        BaseResponse response = new BaseResponse();
        response.setStatus(true);
        response.setDateTime(LocalDateTime.now());
        response.setResult(courseRegistrationRepository.findAll());
        return response;
    }

}
