package com.thn.onlinecoursemanagement.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author ThwetHmueNyein
 * @Date 09/05/2022
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseImageResponse {
    private Long id;
    private String name;
    private LocalDateTime createdAt;
    private String imageUrl;
}
