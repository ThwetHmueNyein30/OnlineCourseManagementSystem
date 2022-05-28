package com.thn.onlinecoursemanagement.ewallet_database.controller;

import com.thn.onlinecoursemanagement.entities.Person;
import com.thn.onlinecoursemanagement.ewallet_database.entities.EWalletInfo;
import com.thn.onlinecoursemanagement.ewallet_database.services.EWalletInfoService;
import com.thn.onlinecoursemanagement.payload.response.BaseResponse;
import com.thn.onlinecoursemanagement.services.KeycloakService;
import lombok.extern.slf4j.Slf4j;
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

    final EWalletInfoService eWalletInfoService;

    final KeycloakService keycloakService;

    public EWalletInfoController(EWalletInfoService eWalletInfoService, KeycloakService keycloakService) {
        this.eWalletInfoService = eWalletInfoService;
        this.keycloakService = keycloakService;
    }

    @GetMapping()
    @CrossOrigin
    @Secured({"ROLE_STUDENT","ROLE_ADMIN"})
    BaseResponse getEWalletInfo() {
        BaseResponse response = new BaseResponse();
        response.setDateTime(LocalDateTime.now());
        Person person=eWalletInfoService.getPerson(keycloakService.getUserKeycloakId());
        if(person==null){
            return new BaseResponse(false, null, LocalDateTime.now(),"No person data found");
        }
        try {
            return new BaseResponse(true,eWalletInfoService.getInfoByPersonId(person.getId()),LocalDateTime.now(),"Success");
        } catch (Exception e) {
            return new BaseResponse(false, null, LocalDateTime.now(),"Fail");
        }
    }

    @PutMapping("{id}")
    @CrossOrigin
    @Secured("ROLE_ADMIN")
    BaseResponse updateEWalletInfo(@PathVariable Long id, @RequestBody EWalletInfo info) {
        return eWalletInfoService.updateEWalletInfo(id,info);
    }

}
