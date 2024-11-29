package io.dynamic.threadpool.starter.monitor;


import io.dynamic.threadpool.common.monitor.Message;

/**
 * 数据采集监控指标分两部分：
 * 线程池任务执行时间 和 线程池运行时的状态
 *
 * 线程池运行时的状态:
 * 当前线程
 * 最大线程
 * 队列容量
 * 队列元素数量
 * 任务总量
 * 拒绝策略执行次数
 * todo 采集的时间和上报的时间
 */
public interface Collect {

    Message collectMessage();

}
