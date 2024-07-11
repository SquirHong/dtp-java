package io.dynamic.threadpool.logrecord.service;

/**
 * 默认实现.
 */
public class DefaultParseFunction implements ParseFunction {

    @Override
    public boolean executeBefore() {
        return true;
    }

    @Override
    public String functionName() {
        return null;
    }

    @Override
    public String apply(String value) {
        return null;
    }

}
