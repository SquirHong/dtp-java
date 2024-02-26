package io.dynamic.threadpool.starter.builder;

import java.io.Serializable;

/**
 * 建造者模式接口定义
 */
public interface Builder<T> extends Serializable {

    /**
     * 构建
     *
     * @return 被构建的对象
     */
    T build();

}