package com.thn.onlinecoursemanagement.ewallet_database.repositories;

/**
 * @author ThwetHmueNyein
 * @Date 04/05/2022
 */
public interface EWalletRepository {
    String deductBalance(Long ownerId, Long courseId);
}
