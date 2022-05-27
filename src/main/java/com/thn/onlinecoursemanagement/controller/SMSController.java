package com.thn.onlinecoursemanagement.controller;

import com.thn.onlinecoursemanagement.entities.SMSHistory;
import com.thn.onlinecoursemanagement.payload.response.BaseResponse;
import com.thn.onlinecoursemanagement.repositories.SMSHistoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author ThwetHmueNyein
 * @Date 03/05/2022
 */

@Slf4j
@RestController
public class SMSController {
    private final SMSHistoryRepository smsHistoryRepository;

    public SMSController(SMSHistoryRepository smsHistoryRepository) {
        this.smsHistoryRepository = smsHistoryRepository;
    }

    @GetMapping()
    @CrossOrigin
    BaseResponse getALlSMS() {
        try {
            return new BaseResponse(true, smsHistoryRepository.findAll(),LocalDateTime.now(),"Success");
        } catch (Exception e) {
            return new BaseResponse(false, null,LocalDateTime.now(),"Fail");
        }
    }

    @DeleteMapping()
    @CrossOrigin
    BaseResponse deleteSMS(@PathVariable Long id) {
        BaseResponse response = new BaseResponse();
        response.setDateTime(LocalDateTime.now());
        try {
            Optional<SMSHistory> smsHistoryOptional = smsHistoryRepository.findById(id);
            if (smsHistoryOptional.isPresent()) {
                SMSHistory smsHistory = smsHistoryOptional.get();
                smsHistoryRepository.deleteById(id);
                return new BaseResponse(true, smsHistory,LocalDateTime.now(),"Successfully deleted");

            } else {
                return new BaseResponse(false, null,LocalDateTime.now(),"No SMS with that ID");

            }
        } catch (Exception e) {
            return new BaseResponse(false, null,LocalDateTime.now(),"Fail to delete");

        }
    }
}
