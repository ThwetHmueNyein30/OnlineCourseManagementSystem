package com.thn.onlinecoursemanagement.controller;

import com.thn.onlinecoursemanagement.repositories.SMSHistoryRepository;
import org.springframework.web.bind.annotation.RestController;

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
}
