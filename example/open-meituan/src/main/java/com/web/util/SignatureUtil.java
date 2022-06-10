package com.web.util;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @ Author : wuheping
 * @ Date   : 2022/2/25
 * @ Desc   : 描述
 */
@Slf4j
public class SignatureUtil {

    private static Gson gson = new Gson();

    // 计算签名
    public static String genSignature(Object obj, String secret) throws Exception {
        Map<String, Object> param = gson.fromJson(gson.toJson(obj), Map.class);
        // 所有参数按参数名排序
        Set<String> keySet = param.keySet();
        List<String> keyList = new ArrayList<>(keySet);
        Collections.sort(keyList);

        // 加密前字符串拼接
        StringBuilder signSB = new StringBuilder();
        for (String key : keyList) {
            if ("sign".equals(key)) {
                continue;
            }
            Object value = param.get(key);
            if (value == null || (value.getClass().isArray() && byte . class .isAssignableFrom(value.getClass().getComponentType()))) {
                continue;
            }
            String valueString = value.toString();
            if (valueString == null || "".equals(valueString.trim())) {
                continue;
            }
            signSB.append(key).append(value);
        }

        // 计算SHA1签名
        String sign = SHA1( secret + signSB.toString()).toLowerCase();
        return sign;
    }

    public static String SHA1(String s) throws Exception {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        byte[] btInput = s.getBytes("utf-8");
        // 获得MD5摘要算法的 MessageDigest 对象
        MessageDigest mdInst = MessageDigest.getInstance("sha-1");
        // 使用指定的字节更新摘要
        mdInst.update(btInput);
        // 获得密文
        byte[] md = mdInst.digest();
        // 把密文转换成十六进制的字符串形式
        int j = md.length;
        char str[] = new char[j * 2];
        int k = 0;
        for (int i = 0; i < j; i++) {
            byte byte0 = md[i];
            str[k++] = hexDigits[byte0 >>> 4 & 0xf];
            str[k++] = hexDigits[byte0 & 0xf];
        }
        return new String(str);
    }
}
