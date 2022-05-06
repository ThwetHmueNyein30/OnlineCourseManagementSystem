package com.thn.onlinecoursemanagement;

import com.thn.onlinecoursemanagement.services.SMSService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class OnlineCourseManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlineCourseManagementApplication.class, args);
    }

    @Bean
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }

}
