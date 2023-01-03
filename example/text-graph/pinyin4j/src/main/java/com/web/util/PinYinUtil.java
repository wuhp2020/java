package com.web.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class PinYinUtil {

    public static void main(String[] args) throws Exception {
        String name = "区平";
        System.out.println(multitone(name));

    }

    public static List<List<String>> multitone (String name) {
        List<List<String>> array = new LinkedList<>();
        Arrays.stream(name.split("")).forEach(s -> {
            try {
                List<String> multitone = Arrays.stream(PinyinHelper.toHanyuPinyinStringArray(s.charAt(0), getDefaultOutputFormat()))
                        .distinct().collect(Collectors.toList());
                array.add(multitone);
            } catch (Exception e) {
                throw new RuntimeException("生成拼音异常");
            }
        });
        return assemble(array);
    }

    private static HanyuPinyinOutputFormat getDefaultOutputFormat() {
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        // 小写
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        // 没有音调数字
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        // u显示
        format.setVCharType(HanyuPinyinVCharType.WITH_U_AND_COLON);
        return format;
    }

    private static List<List<String>> assemble(List<List<String>> array) {
        List<Integer> lengthArr = new LinkedList<>();
        List<Integer> productArr = new LinkedList<>();
        List<List<String>> result = new LinkedList<>();
        int length = 1;
        for (int i = 0; i < array.size(); i++) {
            int len = array.get(i).size();
            lengthArr.add(len);
            int product = i == 0 ? 1 : array.get(i - 1).size() * productArr.get(i - 1);
            productArr.add(product);
            length *= len;
        }
        for (int i = 0; i < length; i++) {
            List<String> resultTemp = new LinkedList<>();
            for (int j = 0; j < array.size(); j++) {
                resultTemp.add(array.get(j).get((int) (Math.floor(i / productArr.get(j)) % lengthArr.get(j))));
            }
            result.add(resultTemp);
        }
        return result;
    }
}
