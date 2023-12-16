package com.zerosx.common.core.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.Random;

/**
 * 高效分布式ID生成算法(sequence),基于Snowflake算法优化实现64位自增ID算法。
 * 其中解决时间回拨问题的优化方案如下：
 * 1. 如果发现当前时间少于上次生成id的时间(时间回拨)，着计算回拨的时间差
 * 2. 如果时间差(offset)小于等于5ms，着等待 offset * 2 的时间再生成
 * 3. 如果offset大于5，则直接抛出异常
 */
@Slf4j
public class IdGenerator {

    private static Sequence WORKER = new Sequence();

    public static long getId() {
        return WORKER.nextId();
    }

    public static String getIdStr() {
        return String.valueOf(WORKER.nextId());
    }

    /**
     * 获取随机数, 不保证唯一
     *
     * @Param: length 随机数长度
     * @return: java.lang.String
     */
    public static String getRandomStr(int length) {
        if (length <= 0) {
            return "";
        }
        length = Math.abs(length);
        Random random = new Random();
        // 获取随机数，去除随机数前两位(0.)
        String randomValue = String.valueOf(random.nextDouble()).substring(2);
        String value = "";
        int maxLength = randomValue.length();
        // 获取随机数字符串长度，并计算需要生成的长度与字符串长度的差值
        int diff = length - maxLength;
        if (diff > 0) {
            // 如果差值大于0，则说明需要生成的串长大于获取的随机数长度，此时需要将最大长度设置为当前随机串的长度
            length = maxLength;
            // 同时递归调用该随机数获取方法，获取剩余长度的随机数
            value += getRandomStr(diff);
        }
        // 获取最终的随机数
        value = randomValue.substring(0, length) + value;
        return value;
    }

    /**
     * 唯一ID
     *
     * @param minLen 最小长度
     * @return 唯一ID
     */
    public static String getIdLen(Integer minLen) {
        String uuid = getIdStr();
        int length = uuid.length();
        if (length >= minLen) {
            return uuid;
        }
        return uuid + IdGenerator.getRandomStr(minLen - length);
    }
}
