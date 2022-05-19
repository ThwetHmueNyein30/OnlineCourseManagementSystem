package com.thn.onlinecoursemanagement.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ThwetHmueNyein
 * @Date 03/05/2022
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SMSRequestBody {
    private String address;
    private String number;
    private String content;
}
