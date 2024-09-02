package io.dynamic.threadpool.config.model.biz.threadpool;

import lombok.Data;

/**
 * ThreadPool del req dto.
 */
@Data
public class ThreadPoolDelReqDTO {

    /**
     * tenantId
     */
    private String tenantId;

    /**
     * itemId
     */
    private String itemId;

    /**
     * tpId
     */
    private String tpId;

}
