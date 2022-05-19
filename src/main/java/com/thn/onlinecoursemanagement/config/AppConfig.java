package com.thn.onlinecoursemanagement.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created By Tungct at 10/03/2021
 */
@Configuration
@Data
@ConfigurationProperties(prefix = "config")
public class AppConfig {
    String uploadFolder;

}