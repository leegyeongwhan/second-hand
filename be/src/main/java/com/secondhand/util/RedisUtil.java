package com.secondhand.util;

public class RedisUtil {

    private static final String ITEM_VIEW_COUNT = "productViewCount";
    private static final String KEY_DELIMITER = "::";

    public static String createItemViewCountCacheKey(Long itemId) {
        return ITEM_VIEW_COUNT + KEY_DELIMITER + itemId;
    }

    public static String getKeyDelimiter() {
        return KEY_DELIMITER;
    }

    public static String getProductViewCountCacheKeyPattern() {
        return ITEM_VIEW_COUNT + "*";
    }
}
