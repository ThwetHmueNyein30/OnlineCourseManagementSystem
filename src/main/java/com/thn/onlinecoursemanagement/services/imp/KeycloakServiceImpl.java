package com.thn.onlinecoursemanagement.services.imp;

import com.thn.onlinecoursemanagement.constants.Util;
import com.thn.onlinecoursemanagement.entities.Person;
import com.thn.onlinecoursemanagement.services.KeycloakService;
import com.thn.onlinecoursemanagement.services.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @author ThwetHmueNyein
 * @Date 23/05/2022
 */

@Service
@Slf4j
public class KeycloakServiceImpl implements KeycloakService {

    final Keycloak keycloak;
    final Util util;
    final RestTemplate restTemplate;
    final Environment environment;
    private final HttpServletRequest request;
    final RoleService roleService;

    public KeycloakServiceImpl(Util util, @Qualifier("Keycloak") Keycloak keycloak, RestTemplate restTemplate, Environment environment, HttpServletRequest request, RoleService roleService) {
        this.util = util;
        this.keycloak = keycloak;
        this.restTemplate = restTemplate;
        this.environment = environment;
        this.request = request;
        this.roleService = roleService;
    }

    private String getRealm(){
        return environment.getProperty("keycloak.realm");
    }
    private String getClientId(){
        return environment.getProperty("keycloak.resource");
    }

    @Override
    public UserRepresentation createUser(Person person) {
        UsersResource usersResource = keycloak.realm(environment.getProperty("keycloak.realm")).users();
        CredentialRepresentation credentialRepresentation = createPasswordCredentials();
        UserRepresentation kcUser = new UserRepresentation();
        kcUser.setCredentials(Collections.singletonList(credentialRepresentation));
        kcUser.setUsername(person.getName());
        kcUser.setEmail(person.getEmail());
        kcUser.setEnabled(true);
        kcUser.setEmailVerified(false);
        String role=roleService.finRoleName(person.getRoleId()).toLowerCase(Locale.ROOT);
        if (role.isEmpty()){
            return null;
        }
        List <String> roleList =new ArrayList<>();
        roleList.add(role);
        kcUser.setRealmRoles(roleList);

        try {
            Response response = usersResource.create(kcUser);
            log.info("Response in keycloak creation : {}", response);
            log.info("Response Status : {}", response.getStatus());
            if (response.getStatus() == 201) {
                List<UserRepresentation> search= keycloak.realm(getRealm()).users().search(kcUser.getUsername());
                kcUser.setId(search.get(0).getId());
                addRealmRoleToUser(kcUser.getUsername(),kcUser.getRealmRoles().get(0));
                return kcUser;
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error("Exception : " + e);
        }
        return kcUser;
    }

    @Override
    public String getUserKeycloakId() {
        return request.getUserPrincipal().getName();
    }

    private static CredentialRepresentation createPasswordCredentials() {
        CredentialRepresentation passwordCredentials = new CredentialRepresentation();
        passwordCredentials.setTemporary(true);
        passwordCredentials.setType(CredentialRepresentation.PASSWORD);
        passwordCredentials.setValue("1234");
        return passwordCredentials;
    }

//    public List<String> getAllRoles(){
//        ClientRepresentation clientRep = keycloak
//                .realm(getRealm())
//                .clients()
//                .findByClientId(getClientId())
//                .get(0);
//
//        return keycloak
//                .realm(getRealm())
//                .clients()
//                .get(clientRep.getId())
//                .roles()
//                .list()
//                .stream()
//                .map(RoleRepresentation::getName)
//                .collect(Collectors.toList());
//    }

//    public void addRealmRole(String new_role_name){
//        if(!getAllRoles().contains(new_role_name)){
//            RoleRepresentation roleRep = new  RoleRepresentation();
//            roleRep.setName(new_role_name);
//            roleRep.setDescription("role_" + new_role_name);
//            ClientRepresentation clientRep = keycloak
//                    .realm(getRealm())
//                    .clients()
//                    .findByClientId(getClientId())
//                    .get(0);
//            keycloak.realm(getRealm())
//                    .clients()
//                    .get(clientRep.getId())
//                    .roles()
//                    .create(roleRep);
//        }
//    }

    public void addRealmRoleToUser(String userName, String role_name){
        log.info("AddRealmRoleToUser : {}", userName + " "+ role_name);
        String client_id = keycloak
                .realm(getRealm())
                .clients()
                .findByClientId(getClientId())
                .get(0)
                .getId();

        String userId = keycloak
                .realm(getRealm())
                .users()
                .search(userName)
                .get(0)
                .getId();
        UserResource user = keycloak
                .realm(getRealm())
                .users()
                .get(userId);
        List<RoleRepresentation> roleToAdd = new LinkedList<>();
        roleToAdd.add(keycloak
                .realm(getRealm())
                .clients()
                .get(client_id)
                .roles()
                .get(role_name)
                .toRepresentation()
        );
        List<RoleRepresentation> roleRepresentationList=new ArrayList<>();
        roleRepresentationList.add(keycloak
                .realm(getRealm())
                .roles()
                .get("training_"+role_name)
                .toRepresentation());
        user.roles().clientLevel(client_id).add(roleToAdd);
        user.roles().realmLevel().add(roleRepresentationList);
    }
}
