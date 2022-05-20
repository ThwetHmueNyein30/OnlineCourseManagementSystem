package com.thn.onlinecoursemanagement.services;

import com.thn.onlinecoursemanagement.entities.Person;
import com.thn.onlinecoursemanagement.payload.response.BaseResponse;
import com.thn.onlinecoursemanagement.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ThwetHmueNyein
 * @Date 19/05/2022
 */

public interface RoleService {

    List<Person> findAllByRole(String role);
}
