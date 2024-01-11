package io.dynamic.threadpool.server.model;

import lombok.Data;

/**
 * 配置信息
 */
@Data
public class ConfigInfo extends ConfigInfoBase {

    private static final long serialVersionUID = -3504960832191834675L;

    private String namespace;
}
