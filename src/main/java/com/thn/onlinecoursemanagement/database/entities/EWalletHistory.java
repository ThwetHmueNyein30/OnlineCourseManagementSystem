package com.thn.onlinecoursemanagement.database.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author ThwetHmueNyein
 * @Date 04/05/2022
 */

//id, walletId, beforeBalance, afterBalance, reason, createdAt, updatedAt
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EWalletHistory {
    private Long id;
    private Long walletId;
    private Double beforeBalance;
    private Double afterBalance;
    private String reason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
