package com.thn.onlinecoursemanagement.payload.resquest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ThwetHmueNyein
 * @Date 03/05/2022
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseRegisterRequestBody {

    private Long studentId;
    private Long courseId;
    private Double fee;
}
