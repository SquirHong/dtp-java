package io.dynamic.threadpool.logrecord.service;


import lombok.AllArgsConstructor;

/**
 * 默认实现函数接口.
 */
@AllArgsConstructor
public class DefaultFunctionServiceImpl implements FunctionService {

    private final ParseFunctionFactory parseFunctionFactory;

    @Override
    public String apply(String functionName, String value) {
        ParseFunction function = parseFunctionFactory.getFunction(functionName);
        if (function == null) {
            return value;
        }
        return function.apply(value);
    }

    @Override
    public boolean beforeFunction(String functionName) {
        return parseFunctionFactory.isBeforeFunction(functionName);
    }

}
