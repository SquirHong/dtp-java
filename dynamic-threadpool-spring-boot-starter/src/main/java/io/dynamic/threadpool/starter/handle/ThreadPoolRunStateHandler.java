package io.dynamic.threadpool.starter.handle;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.system.RuntimeInfo;
import io.dynamic.threadpool.common.model.PoolRunStateInfo;
import io.dynamic.threadpool.starter.core.DynamicThreadPoolExecutor;
import io.dynamic.threadpool.starter.core.GlobalThreadPoolManage;
import io.dynamic.threadpool.starter.toolkit.ByteConvertUtil;
import io.dynamic.threadpool.starter.toolkit.CalculateUtil;
import io.dynamic.threadpool.starter.wrap.DynamicThreadPoolWrapper;
import lombok.extern.slf4j.Slf4j;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池运行状态组件.
 */
@Slf4j
public class ThreadPoolRunStateHandler extends AbstractThreadPoolRuntime {

    private static InetAddress addr;

    static {
        try {
            addr = InetAddress.getLocalHost();

        } catch (UnknownHostException ex) {
            log.error("Local IP acquisition failed.", ex);
        }
    }

    @Override
    protected PoolRunStateInfo supplement(PoolRunStateInfo poolRunStateInfo) {
        // 内存占比: 使用内存 / 最大内存
        RuntimeInfo runtimeInfo = new RuntimeInfo();
        String memoryProportion = StrUtil.builder(
                "已分配: ",
                ByteConvertUtil.getPrintSize(runtimeInfo.getTotalMemory()),
                " / 最大可用: ",
                ByteConvertUtil.getPrintSize(runtimeInfo.getMaxMemory())
        ).toString();

        poolRunStateInfo.setHost(addr.getHostAddress());
        poolRunStateInfo.setMemoryProportion(memoryProportion);
        poolRunStateInfo.setFreeMemory(ByteConvertUtil.getPrintSize(runtimeInfo.getFreeMemory()));
        return poolRunStateInfo;
    }

}
