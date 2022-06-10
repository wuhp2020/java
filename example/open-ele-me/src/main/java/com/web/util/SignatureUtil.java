package com.web.util;

import lombok.extern.slf4j.Slf4j;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ Author : wuheping
 * @ Date   : 2022/2/25
 * @ Desc   : 描述
 */
@Slf4j
public class SignatureUtil {

    // 计算签名
    public static String genSignature(String appId, String merchantId, String accessToken, String timestamp,
                                      String businessData, String appSecret) {
        String sortedParamsStr = SignatureUtil.sortSignatureParams(
                appId, merchantId, timestamp, accessToken, businessData);
        return getSHA256(appSecret + sortedParamsStr);
    }

    public static String genToken(String grantType, String code, String appId, String merchantId,
                                  String timestamp, String appSecret) {
        String sortedParamsStr = SignatureUtil.sortTokenParams(
                grantType, code, appId, merchantId, timestamp);
        return getSHA256(appSecret + sortedParamsStr);
    }

    // 利用java原生的类实现SHA256加密
    private static String getSHA256(String str) {
        MessageDigest messageDigest;
        String encodestr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(str.getBytes("UTF-8"));
            encodestr = byte2Hex(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            log.error("getSHA256 NoSuchAlgorithmException: {}", e.getMessage());
        } catch (UnsupportedEncodingException e) {
            log.error("getSHA256 UnsupportedEncodingException: {}", e.getMessage());
        }
        return encodestr;
    }

    // 将bytes转为16进制
    private static String byte2Hex(byte[] bytes) {
        StringBuffer stringBuffer = new StringBuffer();
        String temp = null;
        for (int i = 0; i < bytes.length; i++) {
            temp = Integer.toHexString(bytes[i] & 0xFF);
            if (temp.length() == 1) {
                // 只有一位则加0补到两位
                stringBuffer.append("0");
            }
            stringBuffer.append(temp);
        }
        return stringBuffer.toString();
    }

    // 按字典序排列参数
    private static String sortParams(Map<String, String> paramMap) {
        List<String> keys = new ArrayList<>(paramMap.keySet());
        Collections.sort(keys);
        StringBuilder content = new StringBuilder();
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = paramMap.get(key);
            if (value != null && value.length() != 0) {
                content.append(i == 0 ? "" : "&").append(key).append("=").append(value);
            }
        }
        return content.toString();
    }

    // 入参TApiParam按字典序排序, 正向业务请求接口入参的加签准备工作
    private static String sortTokenParams(String grantType, String code, String appId, String merchantId, String timestamp) {
        Map<String, String> pMap = new HashMap<>();
        pMap.put("grant_type", grantType);
        pMap.put("code", code);
        pMap.put("app_id", appId);
        pMap.put("merchant_id", merchantId);
        pMap.put("timestamp", timestamp);
        return sortParams(pMap);
    }

    // 入参TApiParam按字典序排序, 正向业务请求接口入参的加签准备工作
    private static String sortSignatureParams(String appId, String merchantId, String timeStamp,
                                        String accessToken, String businessData) {
        Map<String, String> pMap = new HashMap<>();
        pMap.put("app_id", appId);
        pMap.put("merchant_id", merchantId);
        pMap.put("timestamp", timeStamp);
        pMap.put("version", "1.0");
        pMap.put("access_token", accessToken);
        pMap.put("business_data", businessData);
        return sortParams(pMap);
    }

}
