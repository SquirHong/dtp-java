package io.dynamic.threadpool.starter.toolkit;


import cn.hutool.core.util.StrUtil;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import io.dynamic.threadpool.common.config.ApplicationContextHolder;
import io.dynamic.threadpool.starter.config.BootstrapProperties;
import lombok.SneakyThrows;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.ArrayList;

import static io.dynamic.threadpool.common.constant.Constants.GROUP_KEY_DELIMITER;
import static io.dynamic.threadpool.common.constant.Constants.IDENTIFY_SLICER_SYMBOL;
import static io.dynamic.threadpool.starter.config.DynamicThreadPoolAutoConfiguration.CLIENT_IDENTIFICATION_VALUE;


/**
 * Identify util.
 */
public class IdentifyUtil {

    private static String identify;

    public static String generate(ConfigurableEnvironment environment, InetUtils inetUtils) {
        if (StrUtil.isNotBlank(identify)) {
            return identify;
        }
        String ip = inetUtils.findFirstNonLoopbackHostInfo().getIpAddress();
        String port = environment.getProperty("server.port");
        String identification = StrUtil.builder(ip,
                ":",
                port,
                IDENTIFY_SLICER_SYMBOL,
                CLIENT_IDENTIFICATION_VALUE
        ).toString();

        identify = identification;
        return identification;
    }

    public static String getThreadPoolIdentify(String threadPoolId, BootstrapProperties properties) {
        ArrayList<String> params = Lists.newArrayList(
                threadPoolId,
                properties.getItemId(),
                properties.getTenantId(),
                generate(ApplicationContextHolder.getBean(ConfigurableEnvironment.class), ApplicationContextHolder.getBean(InetUtils.class))
        );

        return Joiner.on(GROUP_KEY_DELIMITER).join(params);
    }


}
