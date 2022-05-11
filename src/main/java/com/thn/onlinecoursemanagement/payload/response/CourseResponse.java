package com.thn.onlinecoursemanagement.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author ThwetHmueNyein
 * @Date 09/05/2022
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseResponse extends BaseImageResponse{
    private String content;
    private Double fee;
    private LocalDateTime registeredTo;
    private LocalDateTime registeredFrom;
    private Boolean status;
    private Long teacherId;

    public CourseResponse(Long id, String name, LocalDateTime createdAt, String imageUrl, String content, Double fee, LocalDateTime registeredTo, LocalDateTime registeredFrom, Boolean status, Long teacherId) {
        super(id, name, createdAt, imageUrl);
        this.content = content;
        this.fee = fee;
        this.registeredTo = registeredTo;
        this.registeredFrom = registeredFrom;
        this.status = status;
        this.teacherId = teacherId;
    }
}
