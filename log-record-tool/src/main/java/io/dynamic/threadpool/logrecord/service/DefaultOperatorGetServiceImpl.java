package io.dynamic.threadpool.logrecord.service;


import io.dynamic.threadpool.logrecord.model.Operator;

/**
 * 默认实现.
 */
public class DefaultOperatorGetServiceImpl implements OperatorGetService {

    @Override
    public Operator getUser() {
        return new Operator("994924");
    }

}
