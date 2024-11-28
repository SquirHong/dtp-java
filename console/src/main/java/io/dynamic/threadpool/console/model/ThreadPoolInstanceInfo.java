package io.dynamic.threadpool.console.model;


import io.dynamic.threadpool.config.model.ConfigAllInfo;
import lombok.Data;

/**
 * ThreadPool instance info.
 */
@Data
public class ThreadPoolInstanceInfo extends ConfigAllInfo {

    /**
     * clientAddress
     */
    private String clientAddress;

    /**
     * identify
     */
    private String identify;

    /**
     * clientBasePath
     */
    private String clientBasePath;

}
