package io.dynamic.threadpool.config.model.biz.notify;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.util.List;

/**
 * Alarm query req dto.
 */
@Data
public class NotifyQueryReqDTO extends Page {

    /**
     * groupKeys
     */
    private List<String> groupKeys;

    /**
     * 租户id
     */
    private String tenantId;

    /**
     * 项目id
     */
    private String itemId;

    /**
     * 线程池id
     */
    private String tpId;

}
