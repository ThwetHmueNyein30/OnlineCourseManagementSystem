package com.thn.onlinecoursemanagement.controller;

import com.thn.onlinecoursemanagement.entities.Person;
import com.thn.onlinecoursemanagement.ewallet_database.repositories.EWalletInfoService;
import com.thn.onlinecoursemanagement.payload.response.BaseResponse;
import com.thn.onlinecoursemanagement.services.KeycloakService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * @author ThwetHmueNyein
 * @Date 25/05/2022
 */

@Slf4j
@RestController
@RequestMapping("ewallet-info")
public class EWalletInfoController {

    @Autowired
    EWalletInfoService eWalletInfoService;

    @Autowired
    KeycloakService keycloakService;

    @GetMapping()
    @CrossOrigin
    @Secured({"ROLE_ADMIN"})
    BaseResponse getEWalletInfo() {
        BaseResponse response = new BaseResponse();
        response.setDateTime(LocalDateTime.now());
        Person person=eWalletInfoService.getPerson(keycloakService.getUserKeycloakId());
        try {
            response.setStatus(true);
            response.setResult(eWalletInfoService.getInfoByPersonId(person.getId()));
            response.setMessage("Success");
            return response;
        } catch (Exception e) {
            response.setStatus(false);
            response.setMessage("Failure");
            log.info("Exception : ", e);
            return response;
        }
    }

}
