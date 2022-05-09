package com.thn.onlinecoursemanagement.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ThwetHmueNyein
 * @Date 09/05/2022
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UniversityResponse extends BaseImageResponse{
    private String address;
}
