package io.dynamic.threadpool.common.toolkit;

import io.dynamic.threadpool.common.constant.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

/**
 * Group Key
 */
@Slf4j
public class GroupKey {
    public static String getKey(String dataId, String group) {
        return getKey(dataId, group, "");
    }

    public static String getKey(String dataId, String group, String datumStr) {
        return doGetKey(dataId, group, datumStr);
    }

    public static String getKeyTenant(String dataId, String group, String tenant) {
        return doGetKey(dataId, group, tenant);
    }

    private static String doGetKey(String dataId, String group, String datumStr) {
        StringBuilder sb = new StringBuilder(dataId);
//        urlEncode(dataId, sb);
        sb.append('+');
        urlEncode(group, sb);
        if (!StringUtils.isEmpty(datumStr)) {
            sb.append('+');
            urlEncode(datumStr, sb);
        }

        return sb.toString();
    }

    /**
     * Parse key.
     *
     * @param groupKey group key
     * @return parsed key
     */
    public static String[] parseKey(String groupKey) {
        return groupKey.split(Constants.GROUP_KEY_DELIMITER_TRANSLATION);
    }

    /**
     * + -> %2B
     * % -> %25
     */
    public static void urlEncode(String str, StringBuilder sb) {
        for (int idx = 0; idx < str.length(); ++idx) {
            char c = str.charAt(idx);
            if ('+' == c) {
                sb.append("%2B");

                log.info("出现%2BBBBBBBBBBBBurlEncode: {}", sb);
            } else if ('%' == c) {
                sb.append("%25");
                log.info("出现%2CCCCCCCCCCCCurlEncode: {}", sb);
            } else {
                sb.append(c);
            }
        }
    }
}
