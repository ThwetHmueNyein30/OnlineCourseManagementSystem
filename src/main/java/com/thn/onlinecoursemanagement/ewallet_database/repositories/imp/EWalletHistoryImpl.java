package com.thn.onlinecoursemanagement.ewallet_database.repositories.imp;

import com.thn.onlinecoursemanagement.entities.Course;
import com.thn.onlinecoursemanagement.ewallet_database.entities.EWalletHistory;
import com.thn.onlinecoursemanagement.ewallet_database.entities.EWalletInfo;
import com.thn.onlinecoursemanagement.ewallet_database.pools.EWalletPool;
import com.thn.onlinecoursemanagement.ewallet_database.repositories.EWalletHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static com.thn.onlinecoursemanagement.constant.Constant.*;

/**
 * @author ThwetHmueNyein
 * @Date 09/05/2022
 */

@Component
public class EWalletHistoryImpl implements EWalletHistoryRepository {

    @Autowired
    private final JdbcTemplate template;

    public EWalletHistoryImpl() {
        this.template=new JdbcTemplate(EWalletPool.getInstance().getDataSource());
    }

    @Override
    public List<EWalletHistory> getAllEWalletHistory(Long ewalletId) {
      return  null;
    }

    @Override
    public void insertEWalletHistory(EWalletHistory eWalletHistory) {

    }

}
