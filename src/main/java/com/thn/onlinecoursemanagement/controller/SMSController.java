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
    final SMSHistoryRepository smsHistoryRepository;

    public SMSController(SMSHistoryRepository smsHistoryRepository) {
        this.smsHistoryRepository = smsHistoryRepository;
    }

    @GetMapping()
    @CrossOrigin
    BaseResponse getALlSMS() {
        BaseResponse response = new BaseResponse();
        response.setDateTime(LocalDateTime.now());
        try {
            response.setStatus(true);
            response.setResult(smsHistoryRepository.findAll());
            response.setMessage("Success");
        } catch (Exception e) {
            response.setStatus(false);
            response.setMessage("Fail");
            log.info("Exception : ", e);
        }
        return response;
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
                response.setResult(smsHistory);
                response.setStatus(true);
                response.setMessage("Successfully Deleted.");
            } else {
                response.setStatus(false);
                response.setMessage("No Id found.");
            }
        } catch (Exception e) {
            response.setStatus(false);
            response.setMessage("Fail");
            log.info("Exception : ", e);
        }
        return response;
    }
}
