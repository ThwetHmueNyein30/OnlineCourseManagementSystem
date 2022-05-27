package com.thn.onlinecoursemanagement.services.imp;

import com.thn.onlinecoursemanagement.constants.RoleEnum;
import com.thn.onlinecoursemanagement.constants.Util;
import com.thn.onlinecoursemanagement.entities.Course;
import com.thn.onlinecoursemanagement.entities.CourseRegistration;
import com.thn.onlinecoursemanagement.entities.Person;
import com.thn.onlinecoursemanagement.entities.Role;
import com.thn.onlinecoursemanagement.ewallet_database.entities.EWalletInfo;
import com.thn.onlinecoursemanagement.ewallet_database.services.EWalletInfoService;
import com.thn.onlinecoursemanagement.payload.request.SMSRequestBody;
import com.thn.onlinecoursemanagement.payload.response.BaseResponse;
import com.thn.onlinecoursemanagement.repositories.CourseRegistrationRepository;
import com.thn.onlinecoursemanagement.repositories.CourseRepository;
import com.thn.onlinecoursemanagement.repositories.PersonRepository;
import com.thn.onlinecoursemanagement.repositories.RoleRepository;
import com.thn.onlinecoursemanagement.services.CourseRegistrationService;
import com.thn.onlinecoursemanagement.services.KeycloakService;
import com.thn.onlinecoursemanagement.services.SMSService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.thn.onlinecoursemanagement.constants.Constant.SMS_ADDRESS;

/**
 * @author ThwetHmueNyein
 * @Date 09/05/2022
 */
@Slf4j
@Component
public class CourseRegistrationServiceImpl implements CourseRegistrationService {

    private final CourseRegistrationRepository courseRegistrationRepository;
    private final RoleRepository roleRepository;
    private final PersonRepository personRepository;
    private final CourseRepository courseRepository;
    private final EWalletInfoService eWalletInfoService;
    private final SMSService smsService;
    private final Util util;
    private final KeycloakService keycloakService;

    public CourseRegistrationServiceImpl(CourseRegistrationRepository courseRegistrationRepository, RoleRepository roleRepository, PersonRepository personRepository, CourseRepository courseRepository, EWalletInfoService eWalletInfoService, SMSService smsService, Util util, KeycloakService keycloakService) {
        this.courseRegistrationRepository = courseRegistrationRepository;
        this.roleRepository = roleRepository;
        this.personRepository = personRepository;
        this.courseRepository = courseRepository;
        this.eWalletInfoService = eWalletInfoService;
        this.smsService = smsService;
        this.util = util;
        this.keycloakService = keycloakService;
    }

    public BaseResponse validateRegistration(Long courseId) {
        String keycloakId= keycloakService.getUserKeycloakId();
        if(keycloakId ==null){
            return new BaseResponse(false, null,LocalDateTime.now(),"Don't have any person for that id!" );
        }
       Person person = personRepository.findByKeycloakId(keycloakId);
        if (person==null) {
            return new BaseResponse(false, null,LocalDateTime.now(),"Don't have any person for that id!" );
        }

        Optional<Course> optionalCourse = courseRepository.findById(courseId);
        if (!optionalCourse.isPresent()) {
            return new BaseResponse(false, null,LocalDateTime.now(),"Don't have any course for that id!" );
        }

        Course course = optionalCourse.get();
        if (LocalDateTime.now().isBefore(course.getRegisteredFrom()) || LocalDateTime.now().isAfter(course.getRegisteredTo())) {
            return new BaseResponse(false, null,LocalDateTime.now(),"Cannot register within this time" );
        }

        List<CourseRegistration> courseRegistrationList = courseRegistrationRepository.findAllByPersonId(person.getId());
        if (courseRegistrationList.size() != 0) {
            for (CourseRegistration courseRegistration : courseRegistrationList) {
                if (Objects.equals(courseRegistration.getCourseId(), courseId)) {
                    return new BaseResponse(false, null,LocalDateTime.now(),"Already registered for that course" );
                }
            }
        }
        Optional<Role> optionalRole = roleRepository.findByRoleId(person.getRoleId());
        if (!optionalRole.isPresent()) {
            return new BaseResponse(false, null,LocalDateTime.now(),"Don't have any role for that role id!");
        }
        String roleName = optionalRole.get().getName();
        log.info("RoleName : {}", roleName);
        log.info("Enum  : {}", RoleEnum.STUDENT_ROLE.getCode());
        if (!roleName.equals(RoleEnum.STUDENT_ROLE.getCode())) {
            return new BaseResponse(false, null,LocalDateTime.now(),"Only student can register!");
        }
        EWalletInfo eWalletInfo=eWalletInfoService.getInfoByPersonId(person.getId());
        if (eWalletInfo.getBalance() < course.getFee()) {
            return new BaseResponse(false, null,LocalDateTime.now(),"Not Enough money to register");
        }
        try {
            CourseRegistration courseRegistration = new CourseRegistration(person.getId(), course.getId(), course.getFee(), LocalDateTime.now());
            courseRegistrationRepository.save(courseRegistration);
            //deduct fee from person's eWallet
            eWalletInfoService.deductBalance(person.getId(), course.getId());
            String message = String.format("Thanks for registration this course. You already register  %s, with phone %s .", course.getName(), person.getPhone());
            smsService.createSMS(new SMSRequestBody(SMS_ADDRESS, util.toMsisdn(person.getPhone()), message));
            return new BaseResponse(true, courseRegistration,LocalDateTime.now(),"Successfully registered");
        } catch (Exception e) {
            return new BaseResponse(false, null,LocalDateTime.now(),"Fail to register");
        }
    }

    @Override
    public BaseResponse getAllCourseRegistrationList() {
        try {
            return new BaseResponse(true, courseRegistrationRepository.findAll(),LocalDateTime.now(),"Successful!!");
        } catch (Exception e) {
            return new BaseResponse(false, null,LocalDateTime.now(),"Fail!!");
        }
    }

    @Override
    public BaseResponse getRegisteredCourseByPerson(Long personId) {
        List<Course> courseList = new ArrayList<>();
        List<CourseRegistration> courseRegistration=courseRegistrationRepository.findAllByPersonId(personId);
        for(CourseRegistration c : courseRegistration){
            Optional<Course> optionalCourse=courseRepository.findById(c.getCourseId());
            if(!optionalCourse.isPresent()){
                return new BaseResponse(false, null,LocalDateTime.now(),"No course");
            }
            courseList.add(optionalCourse.get());
        }
        try {
            return new BaseResponse(true, courseList,LocalDateTime.now(),"Success");
        } catch (Exception e) {
            return new BaseResponse(false, null,LocalDateTime.now(),"Fail");
        }
    }

}
