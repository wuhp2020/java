package com.web.util;

import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil extends StringUtils {

    public static int getCharacterPosition(String source, String match, int num){
        //这里是获取"/"符号的位置
        Matcher matcher = Pattern.compile(match).matcher(source);
        int matchNum = 0;
        while(matcher.find()) {
            matchNum++;
            //当"/"符号第三次出现的位置
            if(matchNum == num){
                break;
            }
        }
        if(matchNum != 0){
            return matcher.start();
        } else {
            return -1;
        }
    }
}
