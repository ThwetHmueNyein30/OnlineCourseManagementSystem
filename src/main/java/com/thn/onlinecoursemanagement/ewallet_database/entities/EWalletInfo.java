package com.thn.onlinecoursemanagement.ewallet_database.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * @author ThwetHmueNyein
 * @Date 04/05/2022
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EWalletInfo {

    private Long id;
    private Long ownerId;
    private LocalDateTime createdAt;
    private Double balance;
    private String accountName;

    public EWalletInfo(Long ownerId, LocalDateTime createdAt, Double balance, String accountName) {
        this.ownerId = ownerId;
        this.createdAt = createdAt;
        this.balance = balance;
        this.accountName = accountName;
    }
}
