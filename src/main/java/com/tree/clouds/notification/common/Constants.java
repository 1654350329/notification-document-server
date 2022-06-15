package com.tree.clouds.notification.common;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Constants {
    public static final String TMP_HOME = System.getProperty("java.io.tmpdir") + File.separator;
    public static final String ERROR_LOGIN = "ERROR_LOGIN";
    public static final String LOCK_ACCOUNT = "LOCK_ACCOUNT";

    public static String initMap(int key) {
        Map<Integer, String> map = new HashMap<>();
        map.put(1, "8.3%");
        map.put(2, "16.7%");
        map.put(3, "25.0%");
        map.put(4, "33.3%");
        map.put(5, "41.7%");
        map.put(6, "50%");
        map.put(7, "58.3%");
        map.put(8, "66.7%");
        map.put(9, "75%");
        map.put(10, "83.3%");
        map.put(11, "91.7%");
        map.put(12, "100%");
        return map.get(key);
    }
}
