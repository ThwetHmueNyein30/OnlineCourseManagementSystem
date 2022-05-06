package com.thn.onlinecoursemanagement.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

/**
 * @author ThwetHmueNyein
 * @Date 05/05/2022
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String content;
    private String documentPath;
    private Double fee;
    private LocalDateTime createdAt;
    private LocalDateTime registeredTo;
    private LocalDateTime registeredFrom;
    private Boolean status;
    private Long teacherId;
    private String imageUrl;

    public Course(String name, String content, String documentPath, Double fee, LocalDateTime createdAt, LocalDateTime registeredTo, LocalDateTime registeredFrom, Boolean status, Long teacherId, String imageUrl) {
        this.name = name;
        this.content = content;
        this.documentPath = documentPath;
        this.fee = fee;
        this.createdAt = createdAt;
        this.registeredTo = registeredTo;
        this.registeredFrom = registeredFrom;
        this.status = status;
        this.teacherId = teacherId;
        this.imageUrl = imageUrl;
    }
}
