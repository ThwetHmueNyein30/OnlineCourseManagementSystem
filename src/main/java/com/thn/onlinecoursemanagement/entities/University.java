package com.thn.onlinecoursemanagement.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

/**
 * @author ThwetHmueNyein
 * @Date 02/05/2022
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class University {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String address;
    private LocalDateTime createdAt;
    private String imageUrl;

    public University(String name, String address, LocalDateTime createdAt, String imageUrl) {
        this.name = name;
        this.address = address;
        this.createdAt = createdAt;
        this.imageUrl = imageUrl;
    }
}
