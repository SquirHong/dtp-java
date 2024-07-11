package io.dynamic.threadpool.logrecord.annotation;

/**
 * 日志字段, 用于标记需要比较的实体属性.
 */
public @interface LogField {

    /**
     * 字段名称
     *
     * @return
     */
    String name();

}
