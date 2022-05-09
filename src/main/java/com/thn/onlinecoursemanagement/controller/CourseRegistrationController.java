package com.thn.onlinecoursemanagement.controller;

import com.thn.onlinecoursemanagement.ewallet_database.repositories.EWalletRepository;
import com.thn.onlinecoursemanagement.entities.*;
import com.thn.onlinecoursemanagement.payload.response.BaseResponse;
import com.thn.onlinecoursemanagement.payload.resquest.CourseRegisterRequestBody;
import com.thn.onlinecoursemanagement.constant.RoleEnum;
import com.thn.onlinecoursemanagement.payload.resquest.SMSRequestBody;
import com.thn.onlinecoursemanagement.repositories.*;
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
    RoleRepository roleRepository;
    @Autowired
    PersonRepository personRepository;
    @Autowired
    CourseRepository courseRepository;
    @Autowired
    SMSService smsService;
    @Autowired
    EWalletRepository ewalletRepository;


    @PostMapping()
    BaseResponse registerCourse(@RequestBody CourseRegisterRequestBody courseRegisterRequestBody) {
        BaseResponse response = new BaseResponse();
        response.setStatus(true);
        response.setDateTime(LocalDateTime.now());

        Optional<Person> optionalPerson=personRepository.findById(courseRegisterRequestBody.getStudentId());
        if (!optionalPerson.isPresent()) {
            response.setMessage("Don't have any person for that id!");
            return response;
        }

        Optional<Course> optionalCourse=courseRepository.findById(courseRegisterRequestBody.getCourseId());
        if (!optionalCourse.isPresent()) {
            response.setMessage("Don't have any course for that id!");
            return response;
        }
        Person person=optionalPerson.get();
        Course course=optionalCourse.get();

        if (LocalDateTime.now().isBefore(course.getRegisteredFrom()) || LocalDateTime.now().isAfter(course.getRegisteredTo())) {
            response.setMessage("Cannot Register within this time!");
            return response;
        }
        Optional<Role> optionalRole = roleRepository.findByRoleId(person.getRoleId());

        if (!optionalRole.isPresent()) {
            response.setMessage("Don't have any role for that role id!");
            return response;
        }

        String roleName = optionalRole.get().getName();
        if (!roleName.equals(RoleEnum.STUDENT_ROLE.name())) {
            response.setMessage("Only student can register!");
            return response;
        }

        if(courseRegisterRequestBody.getFee()< course.getFee()){
            response.setMessage("Not Enough money to register");
        }

        if(Objects.equals(courseRegisterRequestBody.getFee(), course.getFee())){
            response.setMessage("Need to pay the correct amount!");
        }

        CourseRegistration courseRegistration = new CourseRegistration(person.getId(), course.getId(), course.getFee());
        courseRegistrationRepository.save(courseRegistration);
        response.setResult(courseRegistration);
        response.setMessage("Successfully registered");

        //deduct fee from person's eWallet
        ewalletRepository.deductBalance(person.getId(), course.getId());

        String message= String.format("Thanks for registration this course. You already register  %s, with phone %s .",course.getName(),person.getPhone());
        String result= smsService.createSMS(new SMSRequestBody(SMS_ADDRESS,person.getPhone(),message));
        log.info("result: {} ",result);
        return response;
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
