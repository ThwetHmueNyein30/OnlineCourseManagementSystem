package com.thn.onlinecoursemanagement.ewallet_database.repositories.imp;

import com.thn.onlinecoursemanagement.entities.Course;
import com.thn.onlinecoursemanagement.ewallet_database.entities.EWalletHistory;
import com.thn.onlinecoursemanagement.ewallet_database.entities.EWalletInfo;
import com.thn.onlinecoursemanagement.ewallet_database.pools.EWalletPool;
import com.thn.onlinecoursemanagement.ewallet_database.repositories.EWalletHistoryRepository;
import com.thn.onlinecoursemanagement.ewallet_database.repositories.EWalletRepository;
import com.thn.onlinecoursemanagement.repositories.CourseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

import static com.thn.onlinecoursemanagement.util.Constant.EWALLET_QUERY;
import static com.thn.onlinecoursemanagement.util.Constant.EWALLET_UPDATE_QUERY;

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

    @Autowired
    EWalletHistoryRepository eWalletHistoryRepository;

    public EWalletImpl() {
        this.template = new JdbcTemplate(EWalletPool.getInstance().getDataSource());
    }

    @Override
    public String deductBalance(Long personId,Long courseId) {
        try {
            List<EWalletInfo> info = template.query(EWALLET_QUERY, new Object[]{personId},
                    (rs, rowNum) ->
                            new EWalletInfo(rs.getLong(1), rs.getLong(2),
                            rs.getTimestamp(3).toLocalDateTime(), rs.getDouble(4),
                            rs.getString(5)));

            log.info("Ewallet Info : {}", info);
            Course course=courseRepository.getById(courseId);

            if (info.isEmpty()){
                return "No person Information";
            }

            EWalletInfo eWalletInfo = info.get(0);

            if(eWalletInfo.getBalance() < course.getFee()){
                return "Not Enough money!";
            }
            Double beforeBalance=eWalletInfo.getBalance();
            Double afterBalance=eWalletInfo.getBalance()-course.getFee();
            String reason="For course registration";
            EWalletHistory eWalletHistory=new EWalletHistory(eWalletInfo.getId(),beforeBalance,afterBalance,reason, LocalDateTime.now(),LocalDateTime.now());
            eWalletHistoryRepository.insertEWalletHistory(eWalletHistory);
            template.update(EWALLET_UPDATE_QUERY, course.getFee(), eWalletInfo.getId());
            return "Already Deducted!";
        }
        catch (Exception e){
            log.info("Exception: ", e);
            return "Error occurred!!";
        }
    }

}
