package io.dynamic.threadpool.logrecord.service;

/**
 * 函数解析.
 */
public interface ParseFunction {

    /**
     * 是否先执行.
     *
     * @return
     */
    default boolean executeBefore() {
        return false;
    }

    /**
     * 函数名称.
     *
     * @return
     */
    String functionName();

    /**
     * 执行.
     *
     * @param value
     * @return
     */
    String apply(String value);

}
