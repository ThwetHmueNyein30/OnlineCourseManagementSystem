package com.thn.onlinecoursemanagement.ewallet_database.services.imp;

import com.thn.onlinecoursemanagement.config.AppConfig;
import com.thn.onlinecoursemanagement.entities.Course;
import com.thn.onlinecoursemanagement.entities.Person;
import com.thn.onlinecoursemanagement.ewallet_database.entities.EWalletHistory;
import com.thn.onlinecoursemanagement.ewallet_database.entities.EWalletInfo;
import com.thn.onlinecoursemanagement.ewallet_database.mapper.EWalletInfoMapper;
import com.thn.onlinecoursemanagement.ewallet_database.pools.EWalletPool;
import com.thn.onlinecoursemanagement.ewallet_database.services.EWalletHistoryService;
import com.thn.onlinecoursemanagement.ewallet_database.services.EWalletInfoService;
import com.thn.onlinecoursemanagement.payload.response.BaseResponse;
import com.thn.onlinecoursemanagement.repositories.CourseRepository;
import com.thn.onlinecoursemanagement.repositories.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
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


    private final JdbcTemplate template;
    private final CourseRepository courseRepository;
    private final EWalletHistoryService eWalletHistoryService;
    private final PersonRepository personRepository;
    final AppConfig appConfig;

    public EWalletInfoImpl(CourseRepository courseRepository, EWalletHistoryService eWalletHistoryService, PersonRepository personRepository, AppConfig appConfig) {
        this.courseRepository = courseRepository;
        this.eWalletHistoryService = eWalletHistoryService;
        this.personRepository = personRepository;
        this.appConfig = appConfig;

        this.template = new JdbcTemplate(EWalletPool.getInstance(appConfig).getDataSource());
    }

    @Override
    public void deductBalance(Long personId, Long courseId) {
        try {
            List<EWalletInfo> info = template.query(appConfig.getEWallet().getInfoQuery(), new Object[]{personId},
                    new EWalletInfoMapper());
            Course course = courseRepository.getById(courseId);
            if (info.isEmpty()) {
                return;
            }
            EWalletInfo eWalletInfo = info.get(0);

            if (eWalletInfo.getBalance() < course.getFee()) {
                return;
            }
            Double beforeBalance = eWalletInfo.getBalance();
            Double afterBalance = eWalletInfo.getBalance() - course.getFee();
            EWalletHistory eWalletHistory = new EWalletHistory(eWalletInfo.getId(), beforeBalance, afterBalance, "For course registration", LocalDateTime.now(), LocalDateTime.now());
            eWalletHistoryService.insertEWalletHistory(eWalletHistory);
            template.update(appConfig.getEWallet().getReduceBalanceQuery(), course.getFee(), eWalletInfo.getId());
        } catch (Exception e) {
            log.info("Exception: ", e);
        }
    }

    @Override
    public EWalletInfo getInfoByPersonId(Long personId) {
        try {
            return template.queryForObject(appConfig.getEWallet().getInfoQuery(), new Object[]{personId}, new EWalletInfoMapper());
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Person getPerson(String keycloakId) {
        return personRepository.findByKeycloakId(keycloakId);
    }

    @Override
    public int insertEWalletIfo(EWalletInfo eWalletInfo) {
        try {
            return template.update(appConfig.getEWallet().getInsertQuery(), eWalletInfo.getOwnerId(), Timestamp.valueOf(eWalletInfo.getCreatedAt()), eWalletInfo.getBalance(), eWalletInfo.getAccountName());
        } catch (Exception e) {
            log.error("Exception  in insertEWalletInfo : " + e);
            return -1;
        }
    }

    @Override
    public int deleteEWalletInfo(Long ownerId) {
        try {
            Object[] args = new Object[]{ownerId};
            return template.update(appConfig.getEWallet().getInfoDeleteQuery(), args);
        } catch (Exception e) {
            log.error("Exception : " + e);
            return -1;
        }
    }

    @Override
    public BaseResponse updateEWalletInfo(Long ownerId, EWalletInfo info) {

        EWalletInfo eWalletInfo;
        try {
            eWalletInfo = template.queryForObject(appConfig.getEWallet().getInfoQuery(), new Object[]{info.getOwnerId()}, new EWalletInfoMapper());

            if (eWalletInfo == null) {
                return new BaseResponse(false, null, LocalDateTime.now(), "No EWalletInfo");
            }
            eWalletInfo.setAccountName(info.getAccountName());
            eWalletInfo.setBalance(info.getBalance());
            eWalletInfo.setCreatedAt(eWalletInfo.getCreatedAt());
            eWalletInfo.setOwnerId(info.getOwnerId());
            eWalletInfo.setBalance(info.getBalance());
            log.info("Update Id: {} ", template.update(appConfig.getEWallet().getInfoUpdateQuery(), eWalletInfo.getOwnerId(), eWalletInfo.getCreatedAt(), eWalletInfo.getBalance(), eWalletInfo.getAccountName(), info.getId()));
            return new BaseResponse(true, eWalletInfo, LocalDateTime.now(), "Successfully updated");
        } catch (Exception e) {
            return new BaseResponse(false, null, LocalDateTime.now(), "Fail to update");
        }
    }
}
