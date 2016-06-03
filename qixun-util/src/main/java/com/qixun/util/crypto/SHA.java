package com.qixun.util.crypto;

import java.security.MessageDigest;

/**
 * Created by guozq on 2016/4/16.
 */
public class SHA {


    /**
     * 使用sha256
     *
     * @param input
     * @param charset
     * @return
     */
    public static String sha256Digest(String input, String charset) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(input.getBytes(charset));

            StringBuilder sb = new StringBuilder(64);
            for (int i = 0; i < bytes.length; i++) {
                String hex = Integer.toHexString(0xff & bytes[i]);
                if (hex.length() == 1) sb.append('0');
                sb.append(hex);
            }

            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



}
