package com.thn.onlinecoursemanagement.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author ThwetHmueNyein
 * @Date 05/05/2022
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonResponse extends BaseImageResponse{
    private Date birthDay;
    private Long roleId;
    private Long universityId;
    private Long companyId;
    private Boolean status;
    private String phone;
    private String email;
    private String address;
    private String keycloakId;

    public PersonResponse(Long id, String name, LocalDateTime createdAt, String imageUrl, Date birthDay, Long roleId, Long universityId, Long companyId, Boolean status, String phone, String email, String address,String keycloakId) {
        super(id, name, createdAt, imageUrl);
        this.birthDay = birthDay;
        this.roleId = roleId;
        this.universityId = universityId;
        this.companyId = companyId;
        this.status = status;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.keycloakId=keycloakId;
    }
}
