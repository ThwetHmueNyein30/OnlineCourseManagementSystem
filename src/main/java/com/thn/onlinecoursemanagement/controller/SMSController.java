package com.thn.onlinecoursemanagement.controller;

import com.thn.onlinecoursemanagement.entities.SMSHistory;
import com.thn.onlinecoursemanagement.payload.response.BaseResponse;
import com.thn.onlinecoursemanagement.repositories.SMSHistoryRepository;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author ThwetHmueNyein
 * @Date 03/05/2022
 */

@RestController
public class SMSController {
    final SMSHistoryRepository smsHistoryRepository;

    public SMSController(SMSHistoryRepository smsHistoryRepository) {
        this.smsHistoryRepository = smsHistoryRepository;
    }

    @GetMapping()
    BaseResponse getALlSMS() {
        BaseResponse response = new BaseResponse();
        response.setStatus(true);
        response.setResult(smsHistoryRepository.findAll());
        response.setMessage("Success");
        return response;
    }

    @DeleteMapping()
    BaseResponse deleteSMS(@PathVariable Long id) {
        BaseResponse response = new BaseResponse();
        response.setStatus(true);
        Optional<SMSHistory> smsHistoryOptional = smsHistoryRepository.findById(id);
        if (smsHistoryOptional.isPresent()) {
            SMSHistory smsHistory = smsHistoryOptional.get();
            smsHistoryRepository.deleteById(id);
            response.setResult(smsHistory);
            response.setMessage("Successfully Deleted.");
        } else {
            response.setMessage("No Id found.");
        }
        return response;
    }
}
