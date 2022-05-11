package com.thn.onlinecoursemanagement.ewallet_database.mapper;

import com.thn.onlinecoursemanagement.ewallet_database.entities.EWalletHistory;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author ThwetHmueNyein
 * @Date 10/05/2022
 */
public class EWalletHistoryMapper implements RowMapper<EWalletHistory> {

    @Override
    public EWalletHistory mapRow(ResultSet rs, int i) throws SQLException {
        EWalletHistory eWalletHistory = new EWalletHistory(rs.getLong(1),
                rs.getLong(2),
                rs.getDouble(3),
                rs.getDouble(4),
                rs.getString(5),
                rs.getTimestamp(6).toLocalDateTime(),
                rs.getTimestamp(7).toLocalDateTime());
        return eWalletHistory;
    }
}

