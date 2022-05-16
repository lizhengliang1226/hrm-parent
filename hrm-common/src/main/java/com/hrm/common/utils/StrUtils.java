package com.hrm.common.utils;

import com.hrm.domain.system.City;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LZL
 * @version v1.0
 * @date 2022/5/16-15:44
 */
public class StrUtils {
    public void split() {

        String b = "北京天津太原呼和石家上海南京杭州宁波济南苏州福州厦门合肥青岛武汉南昌长沙郑州" +
                "广州深圳南宁海口珠海佛山东莞沈阳大连长春哈尔西安银川兰州西宁乌鲁" +
                "重庆成都昆明贵阳";
        final char[] chars = b.toCharArray();
        List<String> aa = new ArrayList();
        for (int i = 0; i < chars.length; i++) {
            if (i > 0 && i % 2 == 1) {
                String t = String.valueOf(String.valueOf(chars[i - 1]) + String.valueOf(chars[i]));
                System.out.println(t);
                aa.add(t);
            }
        }
        System.out.println();
        System.out.println();
        System.out.println();
//        System.out.println(aa[10]);
        for (String s : aa) {
            final City city1 = new City();

            System.out.println(s);
        }
    }
}
