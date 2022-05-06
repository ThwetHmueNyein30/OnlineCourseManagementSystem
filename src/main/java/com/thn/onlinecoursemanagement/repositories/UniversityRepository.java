package com.thn.onlinecoursemanagement.repositories;

import com.thn.onlinecoursemanagement.entities.University;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author ThwetHmueNyein
 * @Date 02/05/2022
 */
public interface UniversityRepository extends JpaRepository<University,Long> {
}
