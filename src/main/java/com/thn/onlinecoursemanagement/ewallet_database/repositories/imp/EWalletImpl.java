package com.thn.onlinecoursemanagement.ewallet_database.repositories.imp;

import com.thn.onlinecoursemanagement.ewallet_database.entities.EWalletHistory;
import com.thn.onlinecoursemanagement.ewallet_database.pools.EWalletPool;
import com.thn.onlinecoursemanagement.ewallet_database.entities.EWalletInfo;
import com.thn.onlinecoursemanagement.ewallet_database.repositories.EWalletRepository;
import com.thn.onlinecoursemanagement.entities.Course;
import com.thn.onlinecoursemanagement.repositories.CourseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Types;
import java.time.LocalDateTime;

import static com.thn.onlinecoursemanagement.constant.Constant.*;

/**
 * @author ThwetHmueNyein
 * @Date 04/05/2022
 */

@Component
@Slf4j
public class EWalletImpl implements EWalletRepository {

    @Autowired
    private final JdbcTemplate template;

    @Autowired
    CourseRepository courseRepository;

    public EWalletImpl() {
        this.template = new JdbcTemplate(EWalletPool.getInstance().getDataSource());
    }

    @Override
    public String deductBalance(Long personId,Long courseId) {

        try {
            EWalletInfo info = template.queryForObject(EWALLET_QUERY, new Object[]{personId},
                    (rs, rowNum) ->
                            new EWalletInfo(rs.getLong(1), rs.getLong(2),
                            rs.getTimestamp(3).toLocalDateTime(), rs.getDouble(4),
                            rs.getString(5)));

            Course course=courseRepository.getById(courseId);
            if (info == null) {
            return "No person Information";
            }
            if(info.getBalance() < course.getFee()){
                return "Not Enough money!";
            }
            Double beforeBalance=info.getBalance();
            Double afterBalance=info.getBalance()-course.getFee();
            String reason="For course registration";
            EWalletHistory eWalletHistory=new EWalletHistory(info.getId(),beforeBalance,afterBalance,reason, LocalDateTime.now(),LocalDateTime.now());

            // define query arguments
            Object[] params = new Object[] { eWalletHistory.getWalletId(), eWalletHistory.getBeforeBalance(), eWalletHistory.getAfterBalance(),eWalletHistory.getReason(),
                    eWalletHistory.getCreatedAt(), eWalletHistory.getUpdatedAt()
            };
            // define SQL types of the arguments
            int[] types = new int[] { Types.BIGINT, Types.DOUBLE, Types.DOUBLE, Types.VARCHAR,Types.TIMESTAMP,Types.TIMESTAMP };

            template.update(EWALLET_HISTORY_INSERT_QUERY,  params,types);
            long id = template.update(EWALLET_UPDATE_QUERY, course.getFee(), info.getId());
            return "Already Deducted!";
        }
        catch (Exception e){
            log.info("Exception: ", e);
            return "Error occurred!!";
        }
    }

}
