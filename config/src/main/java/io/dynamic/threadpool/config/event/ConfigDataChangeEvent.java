package io.dynamic.threadpool.config.event;

import org.springframework.util.StringUtils;

/**
 * Config Data Change Event.
 */
public class ConfigDataChangeEvent extends Event {

    public final String tenantId;

    public final String itemId;

    public final String tpId;

    public final long lastModifiedTs;

    public ConfigDataChangeEvent(String tenantId, String itemId, String tpId, Long gmtModified) {
        if (StringUtils.isEmpty(tenantId) || StringUtils.isEmpty(itemId) || StringUtils.isEmpty(tpId)) {
            throw new IllegalArgumentException("dataId is null or group is null");
        }

        this.tenantId = tenantId;
        this.itemId = itemId;
        this.tpId = tpId;
        this.lastModifiedTs = gmtModified;
    }
}
