package com.ywxt.Utils;

import java.util.HashMap;
import java.util.Map;

public class CommonUtils {

    /**
     * spring入参预处理 Map<String, String[]>=》Map<String, String>
     */
    public static Map<String, String> preSpringParams(Map<String, String[]> params) {
        Map<String, String> map = new HashMap<>();
        for (Map.Entry<String, String[]> m : params.entrySet()) {
            if (!m.getValue()[0].equals("")) {
                map.put(m.getKey(), m.getValue()[0]);
            }
        }
        return map;
    }

}
