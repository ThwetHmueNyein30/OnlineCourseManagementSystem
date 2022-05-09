package com.thn.onlinecoursemanagement.ewallet_database.repositories;

import com.thn.onlinecoursemanagement.ewallet_database.entities.EWalletHistory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author ThwetHmueNyein
 * @Date 09/05/2022
 */

@Component
public interface EWalletHistoryRepository {

    List<EWalletHistory> getAllEWalletHistory(Long id);
    void insertEWalletHistory(EWalletHistory eWalletHistory);

}
