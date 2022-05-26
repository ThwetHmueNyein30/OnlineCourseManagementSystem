package com.thn.onlinecoursemanagement.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author ThwetHmueNyein
 * @Date 02/05/2022
 */

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String address;
    private LocalDateTime createdAt;
    private Date birthDay;
    private Long roleId;
    private Long universityId;
    private Long companyId;
    private Boolean status;
    private String imageUrl;
    private String phone;
    private String email;
    private String keycloakId;

    public Person(String name, String address, LocalDateTime createdAt, Date birthDay, Long roleId, Long universityId, Long companyId, Boolean status, String imageUrl, String phone, String email, String keycloakId) {
        this.name = name;
        this.address = address;
        this.createdAt = createdAt;
        this.birthDay = birthDay;
        this.roleId = roleId;
        this.universityId = universityId;
        this.companyId = companyId;
        this.status = status;
        this.imageUrl = imageUrl;
        this.phone = phone;
        this.email = email;
        this.keycloakId = keycloakId;
    }
}
