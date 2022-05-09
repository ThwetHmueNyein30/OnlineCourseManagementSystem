package com.thn.onlinecoursemanagement.controller;

import com.thn.onlinecoursemanagement.entities.Company;
import com.thn.onlinecoursemanagement.ewallet_database.entities.EWalletHistory;
import com.thn.onlinecoursemanagement.ewallet_database.repositories.EWalletHistoryRepository;
import com.thn.onlinecoursemanagement.payload.response.BaseResponse;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * @author ThwetHmueNyein
 * @Date 09/05/2022
 */

@RestController
@RequestMapping("ewallet-history")
public class EWalletHistoryController {

    private final EWalletHistoryRepository eWalletHistoryRepository;

    public EWalletHistoryController(EWalletHistoryRepository eWalletHistoryRepository) {
        this.eWalletHistoryRepository = eWalletHistoryRepository;
    }

    @GetMapping("{id}")
    BaseResponse getAllEWalletHistory(@PathVariable Long ownerId){
        BaseResponse response=new BaseResponse();
        response.setStatus(true);
        response.setResult(eWalletHistoryRepository.getAllEWalletHistory(ownerId));
        return response;
    }

    @PostMapping
    BaseResponse createEWalletHistory(@RequestBody EWalletHistory eWalletHistory){
        BaseResponse response=new BaseResponse();
        response.setStatus(true);
        if(eWalletHistory==null){
            response.setMessage("No request body");
            return response;
        }
        try{
           eWalletHistoryRepository.insertEWalletHistory(eWalletHistory);
            response.setResult(eWalletHistory);
            response.setMessage("Successfully upload!");
        }catch (Exception e){
            response.setMessage("Fail to upload");
        }
        return response;
    }
}
