package com.thn.onlinecoursemanagement.ewallet_database.repositories.imp;

import com.thn.onlinecoursemanagement.ewallet_database.mapper.EWalletHistoryMapper;
import com.thn.onlinecoursemanagement.ewallet_database.entities.EWalletHistory;
import com.thn.onlinecoursemanagement.ewallet_database.pools.EWalletPool;
import com.thn.onlinecoursemanagement.ewallet_database.repositories.EWalletHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Types;
import java.util.List;

import static com.thn.onlinecoursemanagement.util.Constant.EWALLET_HISTORY_INSERT_QUERY;
import static com.thn.onlinecoursemanagement.util.Constant.EWALLET_HISTORY_QUERY;

/**
 * @author ThwetHmueNyein
 * @Date 09/05/2022
 */


//

@Component
public class EWalletHistoryImpl implements EWalletHistoryRepository {

    @Autowired
    private final JdbcTemplate template;

    public EWalletHistoryImpl() {
        this.template = new JdbcTemplate(EWalletPool.getInstance().getDataSource());
    }

    @Override
    public List<EWalletHistory> getAllEWalletHistory(Long eWalletId) {
        return template.query(EWALLET_HISTORY_QUERY,
                new Object[] { eWalletId },
                new EWalletHistoryMapper());
    }

    @Override
    public void insertEWalletHistory(EWalletHistory eWalletHistory) {
        Object[] params = new Object[] { eWalletHistory.getWalletId(), eWalletHistory.getBeforeBalance(), eWalletHistory.getAfterBalance(),eWalletHistory.getReason(),
                eWalletHistory.getCreatedAt(), eWalletHistory.getUpdatedAt()
        };
        int[] types = new int[] { Types.BIGINT, Types.DOUBLE, Types.DOUBLE, Types.VARCHAR,Types.TIMESTAMP,Types.TIMESTAMP };
        template.update(EWALLET_HISTORY_INSERT_QUERY,  params,types);
    }

}
