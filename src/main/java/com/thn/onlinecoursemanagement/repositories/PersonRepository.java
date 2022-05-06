package com.thn.onlinecoursemanagement.repositories;

import com.thn.onlinecoursemanagement.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author ThwetHmueNyein
 * @Date 02/05/2022
 */
public interface PersonRepository extends JpaRepository<Person,Long> {
}
