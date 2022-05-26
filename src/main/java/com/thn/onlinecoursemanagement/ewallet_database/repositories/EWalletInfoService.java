package com.thn.onlinecoursemanagement.ewallet_database.repositories;

import com.thn.onlinecoursemanagement.entities.Person;
import com.thn.onlinecoursemanagement.ewallet_database.entities.EWalletInfo;
import com.thn.onlinecoursemanagement.payload.response.BaseResponse;

/**
 * @author ThwetHmueNyein
 * @Date 04/05/2022
 */
public interface EWalletInfoService {
    String deductBalance(Long ownerId, Long courseId);
    EWalletInfo getInfoByPersonId (Long personId);
    Person getPerson(String keycloakId);
    void insertEWalletIfo (EWalletInfo eWalletInfo);
}
