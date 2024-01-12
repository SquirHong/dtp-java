package io.dynamic.threadpool.server.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 管理轮训相关的开关
 */
@Slf4j
@Service
public class SwitchService {

    public static final String FIXED_DELAY_TIME = "fixedDelayTime";

    public static final String FIXED_POLLING = "isFixedPolling";

    public static final String FIXED_POLLING_INTERVAL = "fixedPollingInertval";

    private static volatile Map<String, String> switches = new HashMap(16);

    public static int getSwitchInteger(String key, int defaultValue) {
        int rtn = defaultValue;
        try {
            String status = switches.get(key);
            rtn = status != null ? Integer.parseInt(status) : defaultValue;
        } catch (Exception e) {
            rtn = defaultValue;
            log.error("corrupt switch value {}, {}", key, switches.get(key));
        }
        return rtn;
    }

    public static boolean getSwitchBoolean(String key, boolean defaultValue) {
        boolean rtn = defaultValue;
        try {
            String value = switches.get(key);
            rtn = value != null ? Boolean.parseBoolean(value) : defaultValue;
        } catch (Exception e) {
            rtn = defaultValue;
            log.error("corrupt switch value {}, {}", key, switches.get(key));
        }
        return rtn;
    }
}
