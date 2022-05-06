package com.thn.onlinecoursemanagement.database.repositories;

import com.thn.onlinecoursemanagement.database.entities.EWalletInfo;

/**
 * @author ThwetHmueNyein
 * @Date 04/05/2022
 */
public interface EwalletRepository {

    String deductBalance(Long ownerId, Long courseId);
}
