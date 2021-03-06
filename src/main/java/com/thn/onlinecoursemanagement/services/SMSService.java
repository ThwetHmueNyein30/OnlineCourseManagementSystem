package com.thn.onlinecoursemanagement.services;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.thn.onlinecoursemanagement.payload.request.SMSRequestBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
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

    private final RestTemplate restTemplate;

    public SMSService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void createSMS(SMSRequestBody requestBody) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("bulksms", "bulksms");
        HttpEntity<?> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> stringResponseEntity;

        try {
            stringResponseEntity = restTemplate.exchange(BASE_URL, HttpMethod.POST, entity, String.class);
        } catch (Exception e) {
            log.info("Call api fail: ", e);
            return;
        }
        log.info("response: {}", logMessage(stringResponseEntity.getBody()));

        stringResponseEntity.getBody();

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


