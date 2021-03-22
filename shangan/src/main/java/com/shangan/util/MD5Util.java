package com.shangan.util;

import java.security.MessageDigest;

/**
 * @Author Alva
 * @CreateTime 2021/1/26 21:25
 * 处理 MD5 加密的工具类
 */
public class MD5Util {
    private static String byteArrayToHexString(byte[] b) {
        StringBuilder resultstringBuilder = new StringBuilder();
        for (byte value : b) resultstringBuilder.append(byteToHexString(value));

        return resultstringBuilder.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0)
            n += 256;
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    public static String MD5Encode(String origin, String charsetname) {
        String resultString = null;
        try {
            resultString = new String(origin);
            MessageDigest md = MessageDigest.getInstance("MD5");
            if (charsetname == null || "".equals(charsetname))
                resultString = byteArrayToHexString(md.digest(resultString
                        .getBytes()));
            else
                resultString = byteArrayToHexString(md.digest(resultString
                        .getBytes(charsetname)));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return resultString;
    }

    private static final String[] hexDigits = {"0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};
}
