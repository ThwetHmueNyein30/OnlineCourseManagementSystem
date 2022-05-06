package com.thn.onlinecoursemanagement.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author ThwetHmueNyein
 * @Date 29/04/2022
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponse {
    private Boolean status;
    private Object result;
    private LocalDateTime dateTime;
    private String message;

    public void setDateTime() {
        this.dateTime = LocalDateTime.now();
    }
}
