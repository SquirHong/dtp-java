package io.dynamic.threadpool.common.web.base;


import io.dynamic.threadpool.common.web.exception.ErrorCodeEnum;
import io.dynamic.threadpool.common.web.exception.ServiceException;

/**
 * Result 工具类
 */
public final class Results {

    public static Result<Void> success() {
        return new Result<Void>()
                .setCode(Result.SUCCESS_CODE);
    }

    public static <T> Result<T> success(T data) {
        return new Result<T>()
                .setCode(Result.SUCCESS_CODE)
                .setData(data);
    }

    public static <T> Result<T> failure(ServiceException serviceException) {
        return new Result<T>().setCode(ErrorCodeEnum.SERVICE_ERROR.getCode())
                .setMessage(serviceException.getMessage());
    }

    public static <T> Result<T> failure(String code, String message) {
        return new Result<T>()
                .setCode(code)
                .setMessage(message);
    }

    public static <T> Result<T> failure(ErrorCodeEnum errorCode) {
        return new Result<T>()
                .setCode(errorCode.getCode())
                .setMessage(errorCode.getMessage());
    }

}
