package io.dynamic.threadpool.starter.core;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InstanceInfo implements InstanceConfig{

    private static final String UNKNOWN = "unknown";

    private String appName = UNKNOWN;

    private String hostName;

    private String instanceId;
}
