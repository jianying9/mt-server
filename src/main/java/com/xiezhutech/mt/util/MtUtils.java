package com.xiezhutech.mt.util;

import java.util.UUID;

/**
 *
 * @author jianying9
 */
public class MtUtils {

    public static String createUid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
