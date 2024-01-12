package io.dynamic.threadpool.common.web.exception;

/**
 * 异常码
 */
public enum ErrorCode {

    UNKNOWN_ERROR("1", "未知错误"),

    VALIDATION_ERROR("2", "参数错误"),

    SERVICE_ERROR("3", "服务异常");

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}