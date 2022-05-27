package com.thn.onlinecoursemanagement.ewallet_database.services;

import com.thn.onlinecoursemanagement.ewallet_database.entities.EWalletHistory;

import java.util.List;

/**
 * @author ThwetHmueNyein
 * @Date 09/05/2022
 */

public interface EWalletHistoryService {

    List<EWalletHistory> getAllEWalletHistory(Long id);
    void insertEWalletHistory(EWalletHistory eWalletHistory);

}
