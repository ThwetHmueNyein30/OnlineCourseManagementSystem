package com.thn.onlinecoursemanagement.ewallet_database.services;

import com.thn.onlinecoursemanagement.entities.Person;
import com.thn.onlinecoursemanagement.ewallet_database.entities.EWalletInfo;
import com.thn.onlinecoursemanagement.payload.response.BaseResponse;

/**
 * @author ThwetHmueNyein
 * @Date 04/05/2022
 */
public interface EWalletInfoService {
    void deductBalance(Long ownerId, Long courseId);
    EWalletInfo getInfoByPersonId (Long personId);
    Person getPerson(String keycloakId);
    int insertEWalletIfo (EWalletInfo eWalletInfo);
    BaseResponse updateEWalletInfo(Long ownerId, EWalletInfo eWalletInfo);
}
