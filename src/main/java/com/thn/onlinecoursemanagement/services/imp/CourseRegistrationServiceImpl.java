package com.thn.onlinecoursemanagement.services.imp;

import com.thn.onlinecoursemanagement.util.RoleEnum;
import com.thn.onlinecoursemanagement.entities.Course;
import com.thn.onlinecoursemanagement.entities.CourseRegistration;
import com.thn.onlinecoursemanagement.entities.Person;
import com.thn.onlinecoursemanagement.entities.Role;
import com.thn.onlinecoursemanagement.ewallet_database.repositories.EWalletRepository;
import com.thn.onlinecoursemanagement.payload.response.BaseResponse;
import com.thn.onlinecoursemanagement.payload.resquest.CourseRegisterRequestBody;
import com.thn.onlinecoursemanagement.payload.resquest.SMSRequestBody;
import com.thn.onlinecoursemanagement.repositories.CourseRegistrationRepository;
import com.thn.onlinecoursemanagement.repositories.CourseRepository;
import com.thn.onlinecoursemanagement.repositories.PersonRepository;
import com.thn.onlinecoursemanagement.repositories.RoleRepository;
import com.thn.onlinecoursemanagement.services.CourseRegistrationService;
import com.thn.onlinecoursemanagement.services.SMSService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.thn.onlinecoursemanagement.util.Constant.SMS_ADDRESS;

/**
 * @author ThwetHmueNyein
 * @Date 09/05/2022
 */
@Slf4j
@Service
public class CourseRegistrationServiceImpl implements CourseRegistrationService {

    @Autowired
    CourseRegistrationRepository courseRegistrationRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PersonRepository personRepository;
    @Autowired
    CourseRepository courseRepository;
    @Autowired
    EWalletRepository ewalletRepository;
    @Autowired
    SMSService smsService;

    public BaseResponse validateRegistration(CourseRegisterRequestBody courseRegisterRequestBody) {
        BaseResponse response = new BaseResponse();
        response.setDateTime(LocalDateTime.now());
        Optional<Person> optionalPerson = personRepository.findById(courseRegisterRequestBody.getStudentId());
        if (!optionalPerson.isPresent()) {
            response.setStatus(false);
            response.setMessage("Don't have any person for that id!");
            return response;
        }

        Optional<Course> optionalCourse = courseRepository.findById(courseRegisterRequestBody.getCourseId());
        if (!optionalCourse.isPresent()) {
            response.setStatus(false);
            response.setMessage("Don't have any course for that id!");
            return response;
        }

        Person person = optionalPerson.get();
        Course course = optionalCourse.get();
        if (LocalDateTime.now().isBefore(course.getRegisteredFrom()) || LocalDateTime.now().isAfter(course.getRegisteredTo())) {
            response.setStatus(false);
            response.setMessage("Cannot Register within this time!");
            return response;
        }

        List<CourseRegistration> courseRegistrationList = courseRegistrationRepository.findAllByPersonId(person.getId());
        if (courseRegistrationList.size() != 0) {
            for (CourseRegistration courseRegistration : courseRegistrationList) {
                if (Objects.equals(courseRegistration.getCourseId(), courseRegisterRequestBody.getCourseId())) {
                    response.setStatus(false);
                    response.setMessage("Already registered for this course ");
                    return response;
                }
            }
        }

        Optional<Role> optionalRole = roleRepository.findByRoleId(person.getRoleId());
        if (!optionalRole.isPresent()) {
            response.setStatus(false);
            response.setMessage("Don't have any role for that role id!");
            return response;
        }


        String roleName = optionalRole.get().getName();
        if (!roleName.equals(RoleEnum.STUDENT_ROLE.name())) {
            response.setStatus(false);
            response.setMessage("Only student can register!");
            return response;
        }

        if (courseRegisterRequestBody.getFee() < course.getFee()) {
            response.setStatus(false);
            response.setMessage("Not Enough money to register");
        }

        if (Objects.equals(courseRegisterRequestBody.getFee(), course.getFee())) {
            response.setStatus(false);
            response.setMessage("Need to pay the correct amount!");
        }

        try {
            CourseRegistration courseRegistration = new CourseRegistration(person.getId(), course.getId(), course.getFee(), LocalDateTime.now());
            courseRegistrationRepository.save(courseRegistration);
            //deduct fee from person's eWallet
            ewalletRepository.deductBalance(person.getId(), course.getId());
            String message = String.format("Thanks for registration this course. You already register  %s, with phone %s .", course.getName(), person.getPhone());
            smsService.createSMS(new SMSRequestBody(SMS_ADDRESS, person.getPhone(), message));

            response.setResult(courseRegistration);
            response.setStatus(true);
            response.setMessage("Successfully registered");
        } catch (Exception e) {
            response.setStatus(false);
            response.setMessage("Fail to register");
            log.error("Exception : " + e);
        }


        return response;
    }

    @Override
    public BaseResponse getAllCourseRegistrationList() {
        BaseResponse response = new BaseResponse();
        response.setDateTime(LocalDateTime.now());
        try {
            response.setStatus(true);
            response.setDateTime(LocalDateTime.now());
            response.setResult(courseRegistrationRepository.findAll());
            response.setMessage("Success");
        } catch (Exception e) {
            response.setStatus(false);
            response.setMessage("Fail");
            log.error("Exception : " + e);
        }
        return response;
    }

}
