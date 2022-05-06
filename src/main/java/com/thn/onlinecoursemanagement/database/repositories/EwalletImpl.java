package com.thn.onlinecoursemanagement.database.repositories;

import com.thn.onlinecoursemanagement.database.pools.EwalletPool;
import com.thn.onlinecoursemanagement.database.entities.EWalletInfo;
import com.thn.onlinecoursemanagement.entities.Course;
import com.thn.onlinecoursemanagement.repositories.CourseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * @author ThwetHmueNyein
 * @Date 04/05/2022
 */

@Component
@Slf4j
public class EwalletImpl implements EwalletRepository {

    @Autowired
    private final JdbcTemplate template;

    @Autowired
    CourseRepository courseRepository;

    public EwalletImpl() {
        this.template = new JdbcTemplate(EwalletPool.getInstance().getDataSource());
    }

    final String QUERY="select * from ewallet.ewallet_info where ownerId= ?";
    final String UPDATE_QUERY = "update ewallet.ewallet_info set balance = balance - ? where id = ?";



    @Override
    public String deductBalance(Long personId,Long courseId) {
        EWalletInfo info;
        try {
            info = template.queryForObject(QUERY, new Object[]{personId},
                    (rs, rowNum) -> new EWalletInfo(rs.getLong(1), rs.getLong(2), rs.getTimestamp(3).toLocalDateTime(), rs.getDouble(4), rs.getString(5)));
            log.info("query result: {}", info);
            Course course=courseRepository.getById(courseId);
            log.info("course: {} ", course);
            if(info.getBalance()<course.getFee()){
                return "Not Enough money!";
            }
            log.info("Info Balance: {} ", info.getBalance());
            log.info("Course Fee: {}",course.getFee());

            long id = template.update(UPDATE_QUERY, course.getFee(), info.getId());
            log.info("insert id: {}", id);
            return "Already Deducted!";
        }
        catch (Exception e){
            log.info("Exception: {}", e);
            return "Error occurred!!";
        }
    }

}
