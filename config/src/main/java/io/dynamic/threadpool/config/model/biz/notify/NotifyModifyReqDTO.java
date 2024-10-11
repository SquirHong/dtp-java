package io.dynamic.threadpool.config.model.biz.notify;

import lombok.Data;

/**
 * 消息通知入参实体.
 */
@Data
public class NotifyModifyReqDTO {

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

    /**
     * 通知平台
     */
    private String platform;

    /**
     * 通知类型
     */
    private String type;

    /**
     * 密钥
     */
    private String secretKey;

    /**
     * 报警间隔
     */
    private Integer interval;

    /**
     * 接收者
     */
    private String receives;

    /**
     * 是否启用
     */
    private Integer enable;

}