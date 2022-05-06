package com.thn.onlinecoursemanagement.services;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.thn.onlinecoursemanagement.payload.resquest.SMSRequestBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author ThwetHmueNyein
 * @Date 03/05/2022
 */

@Service
@Slf4j
public class SMSService {

    @Autowired
    RestTemplate restTemplate;

    final String baseUrl = "http://10.201.3.251:9898/smppgw/v1.0/action/submit";

    public String createSMS(SMSRequestBody requestBody) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("bulksms", "bulksms");
        HttpEntity<?> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> stringResponseEntity;

        log.info("request: {}", logMessage(entity));
        try {
            stringResponseEntity = restTemplate.exchange(baseUrl, HttpMethod.POST, entity, String.class);
        } catch (Exception e) {
            log.info("Call api fail: ", e);
            return "failed";
        }
        log.info("response: {}", logMessage(stringResponseEntity));

        return stringResponseEntity.getBody();

    }

    public String logMessage(Object o) {
        try {
            return new ObjectMapper().writeValueAsString(o);
        } catch (Exception e) {
            log.error("Cannot parse object to string", e);
            return e.toString();
        }
    }


}


