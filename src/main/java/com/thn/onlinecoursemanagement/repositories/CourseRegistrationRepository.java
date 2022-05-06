package com.thn.onlinecoursemanagement.repositories;

import com.thn.onlinecoursemanagement.entities.CourseRegistration;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author ThwetHmueNyein
 * @Date 02/05/2022
 */

public interface CourseRegistrationRepository extends JpaRepository<CourseRegistration,Long> {
}
