package io.dynamic.threadpool.starter.spi.rejected;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.RejectedExecutionHandler;

/**
 * 自定义拒绝策略
 */
public interface CustomRejectedExecutionHandler {

    /**
     * 生成拒绝策略
     *
     * @return
     */
    RejectedExecutionHandler generateRejected();

    /**
     * 获取类型
     *
     * @return
     */
    Integer getType();

}
