package io.dynamic.threadpool.logrecord.service;


import io.dynamic.threadpool.logrecord.model.Operator;

/**
 * 获取操作人.
 */
public interface OperatorGetService {

    /**
     * 获取操作人.
     *
     * @return
     */
    Operator getUser();

}
