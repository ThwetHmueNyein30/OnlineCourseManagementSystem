package com.thn.onlinecoursemanagement.constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * @author ThwetHmueNyein
 * @Date 02/05/2022
 */
public enum RoleEnum {
        STUDENT_ROLE("STUDENT"),
        TEACHER_ROLE("TEACHER"),
        ADMIN("ADMIN");

        private String code;

        RoleEnum(String code) {
                this.code = code;
        }

        public String getCode() {
                return code;
        }

        public void setCode(String code) {
                this.code = code;
        }

        public static boolean isValidRole(String role) {
                List<String> roleEnumList = new ArrayList<String>(Arrays.asList(STUDENT_ROLE.getCode(), TEACHER_ROLE.getCode(), ADMIN.getCode()));
                return roleEnumList.contains(role.toUpperCase(Locale.ROOT));
        }
}
