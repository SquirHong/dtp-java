package io.dynamic.threadpool.config.toolkit;

import cn.hutool.crypto.digest.DigestUtil;
import io.dynamic.threadpool.common.toolkit.GroupKey;
import io.dynamic.threadpool.config.model.ConfigAllInfo;
import io.dynamic.threadpool.config.service.ConfigCacheService;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.dynamic.threadpool.common.constant.Constants.LINE_SEPARATOR;
import static io.dynamic.threadpool.common.constant.Constants.WORD_SEPARATOR;


/**
 * Md5 配置组件
 */
public class Md5ConfigUtil {

    static final char WORD_SEPARATOR_CHAR = (char) 2;

    static final char LINE_SEPARATOR_CHAR = (char) 1;

    /**
     * 获取 ThreadPool 相关内容 Md5 值
     *
     * @param config
     * @return
     */
    public static String getTpContentMd5(ConfigAllInfo config) {
        StringBuilder stringBuilder = new StringBuilder();
        String targetStr = stringBuilder.append(config.getCoreSize())
                .append(config.getMaxSize())
                .append(config.getQueueType())
                .append(config.getCapacity())
                .append(config.getKeepAliveTime())
                .append(config.getIsAlarm())
                .append(config.getCapacityAlarm())
                .append(config.getLivenessAlarm())
                .toString();
        return DigestUtil.md5Hex(targetStr);
    }

    /**
     * 比较请求的 Md5 与服务端是否一致
     *
     * @param request
     * @param clientMd5Map
     * @return
     */
    public static List<String> compareMd5(HttpServletRequest request, Map<String, String> clientMd5Map) {
        List<String> changedGroupKeys = new ArrayList();
        clientMd5Map.forEach((key, val) -> {
            String remoteIp = RequestUtil.getRemoteIp(request);
            boolean isUpdateData = ConfigCacheService.isUpdateData(key, val, remoteIp);
            if (!isUpdateData) {
                changedGroupKeys.add(key);
            }
        });

        return changedGroupKeys;
    }

    public static Map<String, String> getClientMd5Map(String configKeysString) {
        Map<String, String> md5Map = new HashMap();

        if (null == configKeysString || "".equals(configKeysString)) {
            return md5Map;
        }
        int start = 0;
        List<String> tmpList = new ArrayList(3);
        for (int i = start; i < configKeysString.length(); i++) {
            char c = configKeysString.charAt(i);
            if (c == WORD_SEPARATOR_CHAR) {
                tmpList.add(configKeysString.substring(start, i));
                start = i + 1;
                if (tmpList.size() > 4) {
                    // Malformed message and return parameter error.
                    throw new IllegalArgumentException("invalid protocol,too much key");
                }
            } else if (c == LINE_SEPARATOR_CHAR) {
                String endValue = "";
                if (start + 1 <= i) {
                    endValue = configKeysString.substring(start, i);
                }
                start = i + 1;

                String groupKey = getKey(tmpList.get(0), tmpList.get(1), tmpList.get(2), tmpList.get(3));
                groupKey = SingletonRepository.DataIdGroupIdCache.getSingleton(groupKey);
                md5Map.put(groupKey, endValue);
                tmpList.clear();

                if (md5Map.size() > 10000) {
                    throw new IllegalArgumentException("invalid protocol, too much listener");
                }
            }
        }
        return md5Map;
    }

    public static String getKey(String dataId, String group) {
        StringBuilder sb = new StringBuilder();
        GroupKey.urlEncode(dataId, sb);
        sb.append('+');
        GroupKey.urlEncode(group, sb);
        return sb.toString();
    }

    public static String getKey(String dataId, String group, String tenant, String identify) {
        StringBuilder sb = new StringBuilder();
        GroupKey.urlEncode(dataId, sb);
        sb.append('+');
        GroupKey.urlEncode(group, sb);
        if (!StringUtils.isEmpty(tenant)) {
            sb.append('+');
            GroupKey.urlEncode(tenant, sb);
            sb.append("+")
                    .append(identify);
        }
        return sb.toString();
    }

    public static String compareMd5ResultString(List<String> changedGroupKeys) throws IOException {
        if (null == changedGroupKeys) {
            return "";
        }

        StringBuilder sb = new StringBuilder();

        for (String groupKey : changedGroupKeys) {
            String[] dataIdGroupId = GroupKey.parseKey(groupKey);
            sb.append(dataIdGroupId[0]);
            sb.append(WORD_SEPARATOR);
            sb.append(dataIdGroupId[1]);
            // if have tenant, then set it
            if (dataIdGroupId.length == 3) {
                if (org.apache.commons.lang3.StringUtils.isNotBlank(dataIdGroupId[2])) {
                    sb.append(WORD_SEPARATOR);
                    sb.append(dataIdGroupId[2]);
                }
            }
            sb.append(LINE_SEPARATOR);
        }

        return URLEncoder.encode(sb.toString(), "UTF-8");
    }

}
