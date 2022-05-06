package com.thn.onlinecoursemanagement.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author ThwetHmueNyein
 * @Date 02/05/2022
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String address;
    private LocalDateTime createdAt;
    private String imageUrl;

    public Company(String name, String address, LocalDateTime createdAt, String imageUrl) {
        this.name = name;
        this.address = address;
        this.createdAt = createdAt;
        this.imageUrl = imageUrl;
    }
}
