package io.dynamic.threadpool.starter.alarm;

import lombok.Data;

@Data
public class NotifyConfig {

    /**
     * type
     */
    private String type;

    /**
     * url
     */
    private String url;

    /**
     * token
     */
    private String token;

    /**
     * receives
     */
    private String receives;

}
