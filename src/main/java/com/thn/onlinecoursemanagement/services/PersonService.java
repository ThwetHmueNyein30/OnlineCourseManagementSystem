package com.thn.onlinecoursemanagement.services;

import com.thn.onlinecoursemanagement.entities.Person;
import com.thn.onlinecoursemanagement.payload.request.PersonEWalletRequestBody;
import com.thn.onlinecoursemanagement.payload.response.BaseResponse;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author ThwetHmueNyein
 * @Date 26/05/2022
 */
public interface PersonService {
    BaseResponse createPerson(PersonEWalletRequestBody requestBody);
    BaseResponse deletePerson(Long id);
    BaseResponse uploadImage(Long id, MultipartFile file);
    BaseResponse updatePerson(Long id, PersonEWalletRequestBody personEWalletRequestBody);
    BaseResponse getAllPerson(String role);
    BaseResponse getPersonByName(String userName);
}
