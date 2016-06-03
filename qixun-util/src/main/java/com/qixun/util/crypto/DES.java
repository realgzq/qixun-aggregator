package com.qixun.util.crypto;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by guozq on 2016/01/21.
 * 各种算法工具类，方便使用
 */
public abstract class DES {


    /**
     * 使用3Des进行加密，
     *
     * @param plainText
     * @param charset
     * @return
     */
    public static byte[] des3Encrypt(String plainText, String key, String charset) {

        try {
            SecretKey secretKey = new SecretKeySpec(key.getBytes(charset), "DESede");
/*
            KeyGenerator kg = KeyGenerator.getInstance("DESede");
            kg.init(new SecureRandom(key.getBytes(charset)));
            SecretKey secretKey = kg.generateKey();
*/
            Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            return cipher.doFinal(plainText.getBytes(charset));


        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * 使用3DES进行解密
     *
     * @param cipherText
     * @param key
     * @param charset
     * @return
     */
    public static String des3Decrypt(byte[] cipherText, String key, String charset) {
        try {
            SecretKey secretKey = new SecretKeySpec(key.getBytes(charset), "DESede");

/*
            KeyGenerator kg = KeyGenerator.getInstance("DESede");
            // kg.init(56);
            kg.init(new SecureRandom(key.getBytes(charset)));
            SecretKey secretKey = kg.generateKey();
*/

            Cipher c1 = Cipher.getInstance("DESede/ECB/PKCS5Padding");
            c1.init(Cipher.DECRYPT_MODE, secretKey);    //初始化为解密模式
            byte[] bytes = c1.doFinal(cipherText);
            return new String(bytes, charset);

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }




    public static void main(String[] args) {
        String json = "{\"pin\":\"cgx117\",\"clientKey\":\"insurance_system\",\"testzhongwen\":\"包含有京东商城快捷支付平台，强大的wo83d79f987296fe04af1e3eeeeed6fd178defa209cd4b9de0af20b6317fa84955rker中文字符\"}";
        String charset = "UTF-8";
        String privateKey = "123456789012345678901234";

       /* String cipherText = urlEncode((base64Encode(des3Encrypt(json, privateKey, charset))), charset);
        System.out.println("cipherText:" + cipherText);

        String plainText = des3Decrypt(base64Decode(urlDecode(cipherText, charset)), privateKey, charset);
        System.out.println("plainText:" + plainText);*/


    }
}
