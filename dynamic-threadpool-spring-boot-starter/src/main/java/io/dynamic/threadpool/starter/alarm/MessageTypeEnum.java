package io.dynamic.threadpool.starter.alarm;

/**
 * 消息类型枚举.
 */
public enum MessageTypeEnum {

    /**
     * 通知类型
     */
    CHANGE,

    /**
     * 容量报警
     */
    CAPACITY,

    /**
     * 活跃度报警
     */
    LIVENESS,

    /**
     * 拒绝策略报警
     */
    REJECT

}
