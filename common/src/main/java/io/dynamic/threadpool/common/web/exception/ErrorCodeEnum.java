package io.dynamic.threadpool.common.web.exception;

/**
 * 异常码
 */
public enum ErrorCodeEnum {

    UNKNOWN_ERROR("1", "未知异常"),

    VALIDATION_ERROR("2", "参数异常"),

    SERVICE_ERROR("3", "服务异常"),

    NOT_FOUND ("4", "未找到异常");

    private final String code;

    private final String message;

    ErrorCodeEnum(String code, String message) {
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
