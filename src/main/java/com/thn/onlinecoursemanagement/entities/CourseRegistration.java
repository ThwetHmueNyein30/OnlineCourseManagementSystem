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
 * @Date 02/05/2022
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class CourseRegistration {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long personId;
    private Long courseId;
    private Double fee;
    private LocalDateTime createdAt;

    public CourseRegistration(Long personId, Long courseId, Double fee) {
        this.personId = personId;
        this.courseId = courseId;
        this.fee = fee;
    }
}
