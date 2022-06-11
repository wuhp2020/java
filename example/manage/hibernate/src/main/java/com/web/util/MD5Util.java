package com.web.util;

import java.security.MessageDigest;

/**
 * 加密工具类
 */
public class MD5Util {

    public static String encrypt(String source) throws Exception{
        try {

            MessageDigest md5 = MessageDigest.getInstance("md5");
            byte[] encrypts = md5.digest(source.getBytes("GBK"));
            StringBuffer md5StrBuff = new StringBuffer();

            //将加密后的byte数组转换为十六进制的字符串,否则的话生成的字符串会乱码
            for (int i = 0; i < encrypts.length; i++) {
                if (Integer.toHexString(0xFF & encrypts[i]).length() == 1){
                    md5StrBuff.append("0").append(
                            Integer.toHexString(0xFF & encrypts[i]));
                }else{
                    md5StrBuff.append(Integer.toHexString(0xFF & encrypts[i]));
                }
            }
            return md5StrBuff.toString();
        }catch (Exception e){
            e.printStackTrace();
            throw new Exception("MD5工具类出错");
        }
    }

    public static boolean decipher(String source, String decipher) throws Exception{
        try {
            if(encrypt(source).equals(decipher)){
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new Exception("MD5解密失败");
        }
        return false;
    }
}
