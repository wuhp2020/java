package com.web.util;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

public class AESUtil {

    /**
     * 加密
     * @param encodeRules
     * @param source
     * @return
     */
    public static String encrypt(String encodeRules, String source){
        try {
            KeyGenerator keygen = KeyGenerator.getInstance("AES");
            keygen.init(128, new SecureRandom(encodeRules.getBytes()));
            SecretKey original_key = keygen.generateKey();
            byte [] raw = original_key.getEncoded();
            SecretKey key = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte [] byte_encode = source.getBytes("utf-8");
            byte [] byte_AES = cipher.doFinal(byte_encode);
            String AES_encode = new String(new BASE64Encoder().encode(byte_AES));
            return AES_encode;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 解密
     * @param encodeRules
     * @param cryptograph
     * @return
     */
    public static String decipher(String encodeRules,String cryptograph){
        try {
            KeyGenerator keygen = KeyGenerator.getInstance("AES");
            keygen.init(128, new SecureRandom(encodeRules.getBytes()));
            SecretKey original_key = keygen.generateKey();
            byte [] raw = original_key.getEncoded();
            SecretKey key = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte [] byte_content = new BASE64Decoder().decodeBuffer(cryptograph);
            byte [] byte_decode = cipher.doFinal(byte_content);
            String AES_decode = new String(byte_decode,"utf-8");
            return AES_decode;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
