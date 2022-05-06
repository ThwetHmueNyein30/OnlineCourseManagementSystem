package com.thn.onlinecoursemanagement.repositories;

import com.thn.onlinecoursemanagement.entities.SMSHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author ThwetHmueNyein
 * @Date 03/05/2022
 */

@Repository
public interface SMSHistoryRepository extends JpaRepository<SMSHistory,Long> {

}
