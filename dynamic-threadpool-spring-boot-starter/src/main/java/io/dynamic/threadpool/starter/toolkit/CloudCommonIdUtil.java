package io.dynamic.threadpool.starter.toolkit;

import lombok.SneakyThrows;
import org.springframework.core.env.PropertyResolver;

import java.net.InetAddress;

public class CloudCommonIdUtil {

    private static final String SEPARATOR = ":";

    @SneakyThrows
    public static String getDefaultInstanceId(PropertyResolver resolver) {
        InetAddress host = InetAddress.getLocalHost();
        String hostname = host.getHostAddress();
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
