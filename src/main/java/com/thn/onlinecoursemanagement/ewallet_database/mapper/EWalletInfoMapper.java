package com.thn.onlinecoursemanagement.ewallet_database.mapper;

import com.thn.onlinecoursemanagement.ewallet_database.entities.EWalletHistory;
import com.thn.onlinecoursemanagement.ewallet_database.entities.EWalletInfo;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author ThwetHmueNyein
 * @Date 24/05/2022
 */


public class EWalletInfoMapper implements RowMapper<EWalletInfo> {
    @Override
    public EWalletInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new EWalletInfo(rs.getLong(1), rs.getLong(2),
                rs.getTimestamp(3).toLocalDateTime(), rs.getDouble(4),
                rs.getString(5));
    }
}
