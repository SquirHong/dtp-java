package io.dynamic.threadpool.server.model.biz.tenant;

import lombok.Data;


@Data
public class TenantSaveReqDTO {

    /**
     * 租户ID
     */
    private String tenantId;

    /**
     * 租户名称
     */
    private String tenantName;

    /**
     * 租户简介
     */
    private String tenantDesc;

    /**
     * 负责人
     */
    private String owner;

}
