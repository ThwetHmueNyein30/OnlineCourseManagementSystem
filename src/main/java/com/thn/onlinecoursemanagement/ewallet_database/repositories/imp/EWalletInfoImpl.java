package com.thn.onlinecoursemanagement.ewallet_database.repositories.imp;

import com.thn.onlinecoursemanagement.entities.Course;
import com.thn.onlinecoursemanagement.entities.Person;
import com.thn.onlinecoursemanagement.ewallet_database.entities.EWalletHistory;
import com.thn.onlinecoursemanagement.ewallet_database.entities.EWalletInfo;
import com.thn.onlinecoursemanagement.ewallet_database.mapper.EWalletInfoMapper;
import com.thn.onlinecoursemanagement.ewallet_database.pools.EWalletPool;
import com.thn.onlinecoursemanagement.ewallet_database.repositories.EWalletHistoryService;
import com.thn.onlinecoursemanagement.ewallet_database.repositories.EWalletInfoService;
import com.thn.onlinecoursemanagement.repositories.CourseRepository;
import com.thn.onlinecoursemanagement.repositories.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Types;
import java.time.LocalDateTime;
import java.util.List;

import static com.thn.onlinecoursemanagement.constants.Constant.*;

/**
 * @author ThwetHmueNyein
 * @Date 04/05/2022
 */

@Component
@Slf4j
public class EWalletInfoImpl implements EWalletInfoService {

    @Autowired
    private final JdbcTemplate template;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    EWalletHistoryService eWalletHistoryService;

    @Autowired
    PersonRepository personRepository;

    public EWalletInfoImpl() {
        this.template = new JdbcTemplate(EWalletPool.getInstance().getDataSource());
    }

    @Override
    public String deductBalance(Long personId, Long courseId) {
        try {
            List<EWalletInfo> info = template.query(EWALLET_INFO_QUERY, new Object[]{personId},
                    new EWalletInfoMapper());
            log.info("Ewallet Info : {}", info);
            Course course = courseRepository.getById(courseId);

            if (info.isEmpty()) {
                return "No person Information";
            }

            EWalletInfo eWalletInfo = info.get(0);

            if (eWalletInfo.getBalance() < course.getFee()) {
                return "Not Enough money!";
            }
            Double beforeBalance = eWalletInfo.getBalance();
            Double afterBalance = eWalletInfo.getBalance() - course.getFee();
            String reason = "For course registration";
            EWalletHistory eWalletHistory = new EWalletHistory(eWalletInfo.getId(), beforeBalance, afterBalance, reason, LocalDateTime.now(), LocalDateTime.now());
            eWalletHistoryService.insertEWalletHistory(eWalletHistory);
            template.update(EWALLET_INFO_UPDATE_QUERY, course.getFee(), eWalletInfo.getId());
            return "Already Deducted!";
        } catch (Exception e) {
            log.info("Exception: ", e);
            return "Error occurred!!";
        }
    }

    @Override
    public EWalletInfo getInfoByPersonId(Long personId) {
        return template.queryForObject(EWALLET_INFO_QUERY, new Object[]{personId}, new EWalletInfoMapper());
    }
    @Override
    public Person getPerson(String keycloakId) {
        return personRepository.findByKeycloakId(keycloakId);
    }

    @Override
    public void insertEWalletIfo(EWalletInfo eWalletInfo) {
        Object[] params = new Object[] { eWalletInfo.getOwnerId(),eWalletInfo.getCreatedAt(),eWalletInfo.getBalance(),eWalletInfo.getAccountName()
        };
        int[] types = new int[] { Types.BIGINT, Types.TIMESTAMP,Types.DOUBLE,Types.VARCHAR };
        template.update(EWALLET_INFO_INSERT_QUERY,  params,types);
    }


}
