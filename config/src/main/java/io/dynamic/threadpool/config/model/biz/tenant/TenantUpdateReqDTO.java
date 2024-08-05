package io.dynamic.threadpool.config.model.biz.tenant;

import com.alibaba.fastjson.JSON;
import lombok.Data;


@Data
public class TenantUpdateReqDTO {

    /**
     * id
     */
    private Long id;

    /**
     * 租户 ID
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

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

}
