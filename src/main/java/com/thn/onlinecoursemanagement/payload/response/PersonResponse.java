package com.thn.onlinecoursemanagement.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author ThwetHmueNyein
 * @Date 05/05/2022
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonResponse extends BaseImageResponse{
    private LocalDateTime birthDay;
    private Long roleId;
    private Long universityId;
    private Long companyId;
    private Boolean status;
    private String phone;
    private String email;
}
