package io.dynamic.threadpool.config.model.biz.threadpool;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

/**
 * Thread Pool Query Req DTO.
 */
@Data
public class ThreadPoolQueryReqDTO extends Page {

    /**
     * 租户 ID
     */
    private String tenantId;

    /**
     * 项目 ID
     */
    private String itemId;

    /**
     * 线程池 ID
     */
    private String tpId;

}
