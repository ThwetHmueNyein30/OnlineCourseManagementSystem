package com.thn.onlinecoursemanagement.services;

import com.thn.onlinecoursemanagement.payload.response.BaseResponse;
import com.thn.onlinecoursemanagement.payload.request.CourseRegisterRequestBody;

/**
 * @author ThwetHmueNyein
 * @Date 09/05/2022
 */

public interface CourseRegistrationService {

    BaseResponse validateRegistration(CourseRegisterRequestBody courseRegisterRequestBody);

    BaseResponse getAllCourseRegistrationList();
}
