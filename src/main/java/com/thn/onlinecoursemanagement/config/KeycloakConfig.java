package com.thn.onlinecoursemanagement.config;

import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
/**
 * @author ThwetHmueNyein
 * @Date 23/05/2022
 */

@Configuration
@Slf4j
@Component
@EnableScheduling
public class KeycloakConfig {

    @Autowired
    private Environment env;

    @Bean
    @Qualifier("Keycloak")
    public Keycloak getInstance() {

            return KeycloakBuilder.builder()
                    .realm(env.getProperty("keycloak.realm"))
                    .serverUrl(env.getProperty("keycloak.auth-server-url"))
                    .clientId("admin-cli")
                    .clientSecret(env.getProperty("keycloak.credentials.secret"))
                    .username(env.getProperty("username"))
                    .password(env.getProperty("password"))
                    .build();
    }

}
