package io.dynamic.threadpool.starter.core;

/**
 * 动态线程池配置适配器
 */
//public class ThreadPoolConfigAdapter extends ConfigAdapter {
//
//    @Resource
//    private ThreadPoolOperation threadPoolOperation;
//
//    /**
//     * 执行修改配置的线程池
//     */
//    private ExecutorService executorService = new ThreadPoolExecutor(
//            2,
//            4,
//            0,
//            TimeUnit.SECONDS,
//            new ArrayBlockingQueue(10),
//            new ThreadFactoryBuilder().setNamePrefix("threadPool-config").build(),
//            new ThreadPoolExecutor.DiscardOldestPolicy());
//
//    @Order(1025)
//    @PostConstruct
//    public void subscribeConfig() {
//        Map<String, DynamicThreadPoolWrapper> executorMap =
//                ApplicationContextHolder.getBeansOfType(DynamicThreadPoolWrapper.class);
//
//        List<String> tpIdList = new ArrayList();
//        // 拿到所有线程池id
//        executorMap.forEach((key, val) -> tpIdList.add(val.getTpId()));
//        // 订阅每个配置
//        tpIdList.forEach(each -> threadPoolOperation.subscribeConfig(each, executorService,
//                config -> callbackConfig(config)));
//    }
//
//}
