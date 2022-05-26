package com.thn.onlinecoursemanagement.services;

import com.thn.onlinecoursemanagement.entities.Person;
import org.keycloak.representations.idm.UserRepresentation;

/**
 * @author ThwetHmueNyein
 * @Date 23/05/2022
 */
public interface KeycloakService {
    UserRepresentation createUser(Person person);
    String getUserKeycloakId();
}
