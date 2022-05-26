package com.thn.onlinecoursemanagement.repositories;

import com.thn.onlinecoursemanagement.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author ThwetHmueNyein
 * @Date 02/05/2022
 */
public interface PersonRepository extends JpaRepository<Person,Long> {

//    List<Person> findAllByRoleId(Long id);

    @Query(value = "select p.* from person p, role r where p.role_id = r.role_id and upper(r.name) = ?1 ", nativeQuery = true)
    List<Person> findAllByRole(String role);

    Person findByName(String name);
    Person findByKeycloakId(String keycloakId);

}
