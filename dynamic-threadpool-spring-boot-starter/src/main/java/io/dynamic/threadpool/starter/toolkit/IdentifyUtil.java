package io.dynamic.threadpool.starter.toolkit;


import cn.hutool.core.util.StrUtil;
import org.springframework.core.env.ConfigurableEnvironment;

import static io.dynamic.threadpool.common.constant.Constants.IDENTIFY_SLICER_SYMBOL;
import static io.dynamic.threadpool.starter.config.DynamicThreadPoolAutoConfiguration.CLIENT_IDENTIFICATION_VALUE;


/**
 * Identify util.
 */
public class IdentifyUtil {

    /**
     * Generate identify.
     *
     * @param environment
     * @param hippo4JInetUtils
     * @return
     */
    public static String generate(ConfigurableEnvironment environment, InetUtils hippo4JInetUtils) {
        String ip = hippo4JInetUtils.findFirstNonLoopbackHostInfo().getIpAddress();
        String port = environment.getProperty("server.port");
        String identification = StrUtil.builder(ip,
                ":",
                port,
                IDENTIFY_SLICER_SYMBOL,
                CLIENT_IDENTIFICATION_VALUE
        ).toString();

        return identification;
    }

}
