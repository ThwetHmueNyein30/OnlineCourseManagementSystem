package com.thn.onlinecoursemanagement.payload.request;

import com.thn.onlinecoursemanagement.entities.Person;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ThwetHmueNyein
 * @Date 25/05/2022
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonEWalletRequestBody {
    private Person person;
    private Double balance;
}
