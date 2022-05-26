package com.thn.onlinecoursemanagement.services;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.thn.onlinecoursemanagement.payload.request.SMSRequestBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static com.thn.onlinecoursemanagement.constants.Constant.BASE_URL;

/**
 * @author ThwetHmueNyein
 * @Date 03/05/2022
 */

@Service
@Slf4j
public class SMSService {

    @Autowired
    RestTemplate restTemplate;

    public String createSMS(SMSRequestBody requestBody) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("bulksms", "bulksms");
        HttpEntity<?> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> stringResponseEntity;

        log.info("URL: {}", BASE_URL);
        log.info("request: {}", logMessage(entity));
        try {
            stringResponseEntity = restTemplate.exchange(BASE_URL, HttpMethod.POST, entity, String.class);
        } catch (Exception e) {
            log.info("Call api fail: ", e);
            return "failed";
        }
        log.info("response: {}", logMessage(stringResponseEntity.getBody()));

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


