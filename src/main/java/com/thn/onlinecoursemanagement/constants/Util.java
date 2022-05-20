package com.thn.onlinecoursemanagement.constants;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;

/**
 * @author ThwetHmueNyein
 * @Date 09/05/2022
 */
@Slf4j
public class Util {

     public static String encodeFileToBase64Binary(String imgUrl) {
        try {
            File file = new File(imgUrl);
            FileInputStream fileInputStreamReader = new FileInputStream(file);
            byte[] bytes = new byte[(int) file.length()];
            fileInputStreamReader.read(bytes);
            log.info("Encoded String: {} ", new String(Base64.encodeBase64(bytes), StandardCharsets.UTF_8));
            return new String(Base64.encodeBase64(bytes), StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.info("Exception : ", e);
        }
        return null;
    }


}
