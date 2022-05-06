package com.thn.onlinecoursemanagement.repositories;

import com.thn.onlinecoursemanagement.entities.Company;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author ThwetHmueNyein
 * @Date 02/05/2022
 */
public interface CompanyRepository extends JpaRepository<Company,Long> {
}
