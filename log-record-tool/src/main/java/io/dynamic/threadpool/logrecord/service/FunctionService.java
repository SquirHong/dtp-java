package io.dynamic.threadpool.logrecord.service;

/**
 * 函数服务.
 */
public interface FunctionService {

    /**
     * 执行.
     *
     * @param functionName
     * @param value
     * @return
     */
    String apply(String functionName, String value);

    /**
     * 是否提前执行.
     *
     * @param functionName
     * @return
     */
    boolean beforeFunction(String functionName);

}
