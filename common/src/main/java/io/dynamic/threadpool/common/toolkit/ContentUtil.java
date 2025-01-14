package io.dynamic.threadpool.common.toolkit;

import com.alibaba.fastjson.JSON;
import io.dynamic.threadpool.common.constant.Constants;
import io.dynamic.threadpool.common.model.PoolParameter;
import io.dynamic.threadpool.common.model.PoolParameterInfo;

/**
 * Content Util.
 */
public class ContentUtil {

    public static String getPoolContent(PoolParameter parameter) {
        PoolParameterInfo poolInfo = new PoolParameterInfo();
        poolInfo.setTenantId(parameter.getTenantId());
        poolInfo.setItemId(parameter.getItemId());
        poolInfo.setTpId(parameter.getTpId());
        poolInfo.setCoreSize(parameter.getCoreSize());
        poolInfo.setMaxSize(parameter.getMaxSize());
        poolInfo.setQueueType(parameter.getQueueType());
        poolInfo.setCapacity(parameter.getCapacity());
        poolInfo.setKeepAliveTime(parameter.getKeepAliveTime());
        poolInfo.setRejectedType(parameter.getRejectedType());
        poolInfo.setIsAlarm(parameter.getIsAlarm());
        poolInfo.setCapacityAlarm(parameter.getCapacityAlarm());
        poolInfo.setLivenessAlarm(parameter.getLivenessAlarm());
        return JSON.toJSONString(poolInfo);
    }

    /**
     * 拼接tpid+itemid+tenantId
     */
    public static String getGroupKey(PoolParameter parameter) {
        StringBuilder stringBuilder = new StringBuilder();
        String resultStr = stringBuilder
                .append(parameter.getTpId())
                .append(Constants.GROUP_KEY_DELIMITER)
                .append(parameter.getItemId())
                .append(Constants.GROUP_KEY_DELIMITER)
                .append(parameter.getTenantId())
                .toString();
        return resultStr;
    }

    /**
     * 拼接parameters 参数
     */
    public static String getGroupKey(String... parameters) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < parameters.length; i++) {
            stringBuilder.append(parameters[i]);

            if (i < parameters.length - 1) {
                stringBuilder.append(Constants.GROUP_KEY_DELIMITER);
            }
        }

        return stringBuilder.toString();
    }
}
