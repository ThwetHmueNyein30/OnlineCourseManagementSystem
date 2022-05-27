package com.thn.onlinecoursemanagement.ewallet_database.services.imp;

import com.thn.onlinecoursemanagement.config.AppConfig;
import com.thn.onlinecoursemanagement.ewallet_database.mapper.EWalletHistoryMapper;
import com.thn.onlinecoursemanagement.ewallet_database.entities.EWalletHistory;
import com.thn.onlinecoursemanagement.ewallet_database.pools.EWalletPool;
import com.thn.onlinecoursemanagement.ewallet_database.services.EWalletHistoryService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.sql.Types;
import java.util.List;

/**
 * @author ThwetHmueNyein
 * @Date 09/05/2022
 */

@Component
public class EWalletHistoryImpl implements EWalletHistoryService {

    private final JdbcTemplate template;
    final AppConfig appConfig;

    public EWalletHistoryImpl(AppConfig appConfig) {
        this.appConfig = appConfig;
        this.template = new JdbcTemplate(EWalletPool.getInstance(appConfig).getDataSource());
    }

    @Override
    public List<EWalletHistory> getAllEWalletHistory(Long eWalletId) {
        return template.query(appConfig.getEWallet().getHistoryQuery(),
                new Object[] { eWalletId },
                new EWalletHistoryMapper());
    }

    @Override
    public void insertEWalletHistory(EWalletHistory eWalletHistory) {
        Object[] params = new Object[] { eWalletHistory.getWalletId(), eWalletHistory.getBeforeBalance(), eWalletHistory.getAfterBalance(),eWalletHistory.getReason(),
                Timestamp.valueOf(eWalletHistory.getCreatedAt()), Timestamp.valueOf(eWalletHistory.getUpdatedAt())
        };
        int[] types = new int[] { Types.BIGINT, Types.DOUBLE, Types.DOUBLE, Types.VARCHAR,Types.TIMESTAMP,Types.TIMESTAMP };
        template.update(appConfig.getEWallet().getHistoryInsert(),  params,types);
    }

}
