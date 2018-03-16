package com.easy.ms.router.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.util.*;

import org.apache.commons.lang3.StringUtils;

/**
 * User: rizenguo
 * Date: 2014/10/23
 * Time: 15:43
 */
public class MD5 {
    private final static String[] hexDigits = {"0", "1", "2", "3", "4", "5", "6", "7",
            "8", "9", "a", "b", "c", "d", "e", "f"};
    private final static char[] hexDigits_2 = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F'};

    /**
     * 转换字节数组为16进制字串
     *
     * @param b 字节数组
     * @return 16进制字串
     */
    public static String byteArrayToHexString(byte[] b) {
        StringBuilder resultSb = new StringBuilder();
        for (byte aB : b) {
            resultSb.append(byteToHexString(aB));
        }
        return resultSb.toString();
    }

    /**
     * 转换byte到16进制
     *
     * @param b 要转换的byte
     * @return 16进制格式
     */
    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n = 256 + n;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    /**
     * MD5编码
     *
     * @param origin 原始字符串
     * @return 经过MD5加密之后的结果
     */
    public static String MD5Encode(String origin) {
        String resultString = null;
        try {
            resultString = origin;
            MessageDigest md = MessageDigest.getInstance("MD5");
            resultString = byteArrayToHexString(md.digest(resultString.getBytes("UTF-8")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultString;
    }

    public static final String toMD5(String s) {
        try {
            byte[] btInput = s.getBytes("UTF-8");
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);

            byte[] md = mdInst.digest();

            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[(k++)] = hexDigits_2[(byte0 >>> 4 & 0xF)];
                str[(k++)] = hexDigits_2[(byte0 & 0xF)];
            }
            return new String(str).toUpperCase(Locale.US);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * 对实体所有属性进行加密，并按照属性名排序
     *
     * @param t
     * @param ignoreField 不需要加密的字段
     * @return
     */
    public static <T> String getSignByClass(T t, String key, String... ignoreField) {
        StringBuffer signSb = new StringBuffer();
        try {
            Field[] field = t.getClass().getDeclaredFields();
            List<Field> names = new ArrayList<Field>();
            names = Arrays.asList(field);
            Collections.sort(names, new Comparator<Field>() {
                @Override
                public int compare(Field f1, Field f2) {
                    return f1.getName().compareTo(f2.getName());
                }
            });

            List<String> ignores = new ArrayList<String>();
            if (null != ignoreField && 1 <= ignoreField.length) {
                ignores = Arrays.asList(ignoreField);
            }

            for (int j = 0; j < names.size(); j++) {
                String name = names.get(j).getName();

                if (ignores.contains(name)) {
                    continue;
                }
                name = name.substring(0, 1).toUpperCase() + name.substring(1);
                Method m = t.getClass().getMethod("get" + name);
                String value = (String) m.invoke(t);
                if (value != null) {
                    signSb.append(value);
                }
            }
            if(StringUtils.isNotEmpty(key)) {
                signSb.append(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return MD5.toMD5(signSb.toString());
    }
}
