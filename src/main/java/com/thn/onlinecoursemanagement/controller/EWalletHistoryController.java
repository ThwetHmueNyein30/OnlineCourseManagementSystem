package com.thn.onlinecoursemanagement.controller;

import com.thn.onlinecoursemanagement.ewallet_database.entities.EWalletHistory;
import com.thn.onlinecoursemanagement.ewallet_database.repositories.EWalletHistoryService;
import com.thn.onlinecoursemanagement.payload.response.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * @author ThwetHmueNyein
 * @Date 09/05/2022
 */

@Slf4j
@RestController
@RequestMapping("ewallet-history")
public class EWalletHistoryController {

    private final EWalletHistoryService eWalletHistoryService;

    public EWalletHistoryController(EWalletHistoryService eWalletHistoryService) {
        this.eWalletHistoryService = eWalletHistoryService;
    }

    @GetMapping("{id}")
    @CrossOrigin
    @Secured({"ROLE_STUDENT"})
    BaseResponse getAllEWalletHistory(@PathVariable Long id) {
        BaseResponse response = new BaseResponse();
        response.setDateTime(LocalDateTime.now());
        try {
            response.setStatus(true);
            response.setResult(eWalletHistoryService.getAllEWalletHistory(id));
            response.setMessage("Success");
            return response;
        } catch (Exception e) {
            response.setStatus(false);
            response.setMessage("Failure");
            log.info("Exception : ", e);
            return response;
        }
    }

    @PostMapping
    @CrossOrigin
    @Secured({"ROLE_ADMIN"})
    BaseResponse createEWalletHistory(@RequestBody EWalletHistory eWalletHistory) {
        BaseResponse response = new BaseResponse();
        response.setDateTime(LocalDateTime.now());
        if (eWalletHistory == null) {
            response.setStatus(false);
            response.setMessage("No request body");
            return response;
        }
        try {
            eWalletHistoryService.insertEWalletHistory(eWalletHistory);
            response.setResult(eWalletHistory);
            response.setStatus(true);
            response.setMessage("Successfully upload!");
        } catch (Exception e) {
            response.setStatus(false);
            response.setMessage("Fail to upload");
            log.info("Exception : ", e);
        }
        return response;
    }
}
