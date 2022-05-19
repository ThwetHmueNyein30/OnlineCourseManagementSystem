package com.thn.onlinecoursemanagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ThwetHmueNyein
 * @Date 19/05/2022
 */
@Configuration
public class CorsConfig {
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        List<String> allowed = new ArrayList<>();
        allowed.add("*");
        configuration.setAllowedOrigins(allowed);
        configuration.setAllowedMethods(allowed);
        List<String> headers = new ArrayList<>();
        headers.add("Authorization");
        headers.add("Cache-Control");
        headers.add("Content-Type");
        configuration.setAllowedHeaders(headers);
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
