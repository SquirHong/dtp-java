package io.dynamic.threadpool.starter.core;

/**
 * 线程池启动监听
 */
//@Slf4j
//public class ThreadPoolRunListener {
//
//    private final DynamicThreadPoolProperties properties;
//
//
//    public ThreadPoolRunListener(DynamicThreadPoolProperties properties) {
//        this.properties = properties;
//    }
//
//    @Order(1024)
//    @PostConstruct
//    public void run() {
//        Map<String, DynamicThreadPoolWrapper> executorMap = ApplicationContextHolder.getBeansOfType(DynamicThreadPoolWrapper.class);
//
//        executorMap.forEach((key, val) -> {
//
//            Map<String, String> queryStrMap = new HashMap();
//            queryStrMap.put("tpId", val.getTpId());
//            queryStrMap.put("itemId", properties.getItemId());
//            queryStrMap.put("tenantId", properties.getTenantId());
//
//            PoolParameterInfo ppi = new PoolParameterInfo();
//            HttpAgent httpAgent = new ServerHttpAgent(properties);
//            Result result = null;
//            try {
//                result = httpAgent.httpGet(Constants.CONFIG_CONTROLLER_PATH, null, queryStrMap, 3000L);
//                // 如果数据库有值，则将得到的参数转化为PoolParameterInfo，         没指定的tpid，则使用默认的
//                if (result.isSuccess() && (ppi = JSON.toJavaObject((JSON) result.getData(), PoolParameterInfo.class)) != null) {
//                    // 使用相关参数创建线程池
//                    BlockingQueue workQueue = QueueTypeEnum.createBlockingQueue(ppi.getQueueType(), ppi.getCapacity());
//                    RejectedExecutionHandler rejectedExecutionHandler = RejectedTypeEnum.createPolicy(ppi.getRejectedType());
//                    ThreadPoolExecutor poolExecutor = ThreadPoolBuilder.builder()
//                            .isCustomPool(true)
//                            .poolThreadSize(ppi.getCoreSize(), ppi.getMaxSize())
//                            .keepAliveTime(ppi.getKeepAliveTime(), TimeUnit.SECONDS)
//                            .workQueue(workQueue)
//                            .threadFactory(val.getTpId())
//                            .rejected(rejectedExecutionHandler)
//                            .build();
//                    val.setPool(poolExecutor);
//                } else if (val.getPool() == null) {
//                    val.setPool(CommonDynamicThreadPool.getInstance(val.getTpId()));
//                }
//            } catch (Exception ex) {
//                log.error("[Init pool] Failed to initialize thread pool configuration. error message :: {}", ex.getMessage());
//                val.setPool(CommonDynamicThreadPool.getInstance(val.getTpId()));
//            }
//
//            GlobalThreadPoolManage.register(val.getTpId(), ppi, val);
//            log.info("[Init pool] Thread pool initialization completed. tpId :: {},ppi :: {}", val.getTpId(),ppi);
//        });
//    }
//
//}
