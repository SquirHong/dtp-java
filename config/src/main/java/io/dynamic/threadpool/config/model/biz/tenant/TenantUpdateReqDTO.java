package io.dynamic.threadpool.config.model.biz.tenant;

import lombok.Data;


@Data
public class TenantUpdateReqDTO {

    /**
     * 租户 ID
     */
    private String namespaceId;

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
