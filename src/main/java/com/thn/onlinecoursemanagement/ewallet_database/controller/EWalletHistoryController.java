package com.thn.onlinecoursemanagement.ewallet_database.controller;

import com.thn.onlinecoursemanagement.ewallet_database.entities.EWalletHistory;
import com.thn.onlinecoursemanagement.ewallet_database.services.EWalletHistoryService;
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
        try {
            return new BaseResponse(true,eWalletHistoryService.getAllEWalletHistory(id),LocalDateTime.now(),"Success");
        } catch (Exception e) {
            return new BaseResponse(false,null,LocalDateTime.now(),"Fail");
        }
    }

    @PostMapping
    @CrossOrigin
    @Secured({"ROLE_ADMIN"})
    BaseResponse createEWalletHistory(@RequestBody EWalletHistory eWalletHistory) {
        if (eWalletHistory == null) {
            return new BaseResponse(false,null,LocalDateTime.now(),"No request Body");
        }
        try {
            eWalletHistoryService.insertEWalletHistory(eWalletHistory);
            return new BaseResponse(true,eWalletHistory,LocalDateTime.now(),"Insert data success");
        } catch (Exception e) {
            return new BaseResponse(false,null,LocalDateTime.now(),"Fail");
        }
    }
}
