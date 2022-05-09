package com.thn.onlinecoursemanagement.ewallet_database.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author ThwetHmueNyein
 * @Date 04/05/2022
 */

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

    public EWalletHistory(Long walletId, Double beforeBalance, Double afterBalance, String reason, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.walletId = walletId;
        this.beforeBalance = beforeBalance;
        this.afterBalance = afterBalance;
        this.reason = reason;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
