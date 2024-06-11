package io.dynamic.threadpool.starter.toolkit;

import org.springframework.core.env.PropertyResolver;

public class CloudCommonIdUtil {

    private static final String SEPARATOR = ":";

    public static String getDefaultInstanceId(PropertyResolver resolver) {
        String hostname = resolver.getProperty("spring.cloud.client.hostname");
        String appName = resolver.getProperty("spring.application.name");
        String namePart = combineParts(hostname, SEPARATOR, appName);
        String indexPart = resolver.getProperty("spring.application.instance_id", resolver.getProperty("server.port"));
        return combineParts(namePart, SEPARATOR, indexPart);
    }

    public static String combineParts(String firstPart, String separator, String secondPart) {
        if (firstPart != null && secondPart != null) {
            return firstPart + separator + secondPart;
        }
        return firstPart != null ? firstPart : secondPart;
    }

}
