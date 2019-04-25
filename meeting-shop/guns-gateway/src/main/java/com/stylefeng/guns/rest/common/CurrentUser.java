package com.stylefeng.guns.rest.common;

/**
 * @author junyong.chen
 * @date 2019/4/3 20:38
 * @description 当前用户信息的工具类
 */
public class CurrentUser {
    // 线程绑定的存储空间
    private static final ThreadLocal<String> threadLocal = new ThreadLocal<>();

    private CurrentUser() {}

    /**
     * 将用户信息放入存储空间
     * @param userId
     */
    public static void saveUserId(String userId) {
        threadLocal.set(userId);
    }

    /**
     * 将用户信息取出
     * @return
     */
    public static String getCurrentUser() {
        return threadLocal.get();
    }
}
