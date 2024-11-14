package com.vrlease.util;


public class RedisConstants {
    public static final String LOGIN_CODE_KEY = "login:code:";
    public static final Long LOGIN_CODE_TTL = 2L;
    public static final String LOGIN_USER_KEY = "login:token:";
    public static final Long LOGIN_USER_TTL = 36000L;

    public static final Long CACHE_NULL_TTL = 2L;

    public static final Long CACHE_SHOP_TTL = 30L;
    public static final String CACHE_SHOP_KEY = "cache:shop:";

    public static final String LOCK_SHOP_KEY = "lock:shop:";
    public static final Long LOCK_SHOP_TTL = 10L;

    public static final String SECKILL_STOCK_KEY = "seckill:stock:";
    public static final String BLOG_LIKED_KEY = "blog:liked:";
    public static final String FEED_KEY = "feed:";
    public static final String SHOP_GEO_KEY = "shop:geo:";
    public static final String USER_SIGN_KEY = "sign:";

    public static final String LOGIN_CODE_LAST_TIME = "login:code:lasttime:";
    public static final Long CODE_INTERVAL_TIME = 30L;  // 验证码发送间隔时间，单位秒

    public static final String DEVICE_KEY = "device:";
    public static final String DEVICE_BRAND_KEY = "device:brand:";

    public static final String USER_KEY = "user:";

    public static final String ORDER_HISTORY_KEY = "order:history:";
}
