package com.stylefeng.guns.core.util;

import java.util.UUID;

/**
 * @author junyong.chen
 * @date 2019/4/13 10:36
 * @description
 */
public class UUIDUtil {

    public static String genUuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
