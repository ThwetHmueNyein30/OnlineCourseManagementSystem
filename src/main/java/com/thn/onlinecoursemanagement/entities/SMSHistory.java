package com.thn.onlinecoursemanagement.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author ThwetHmueNyein
 * @Date 03/05/2022
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class SMSHistory {

    @Id
    @GeneratedValue
    private Long id;
    private String alias;
    private String msisdn;
    private String createdAt;
    private String smsId;
}
