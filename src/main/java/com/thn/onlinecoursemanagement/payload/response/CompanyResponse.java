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
public class CompanyResponse extends BaseImageResponse{
    private String address;

    public CompanyResponse(Long id, String name, LocalDateTime createdAt, String imageUrl, String address) {
        super(id, name, createdAt, imageUrl);
        this.address = address;
    }
}
