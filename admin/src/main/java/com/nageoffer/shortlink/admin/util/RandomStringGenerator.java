package com.nageoffer.shortlink.admin.util;

import java.util.Random;

/**
 * 生成6位随机数
 */
public final class RandomStringGenerator {
    // 定义字符池，包括数字和英文字母
    private static final String CHAR_POOL = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    // 定义随机数生成器
    private static final Random random = new Random();

    // 生成指定长度的随机字符串
    public static String generateRandomString(int length) {
        StringBuilder stringBuilder = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            // 从字符池中随机选择一个字符
            int index = random.nextInt(CHAR_POOL.length());
            stringBuilder.append(CHAR_POOL.charAt(index));
        }

        return stringBuilder.toString();
    }

}
