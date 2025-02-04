package io.dynamic.threadpool.config.service.biz;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.dynamic.threadpool.config.model.biz.threadpool.ThreadPoolDelReqDTO;
import io.dynamic.threadpool.config.model.biz.threadpool.ThreadPoolQueryReqDTO;
import io.dynamic.threadpool.config.model.biz.threadpool.ThreadPoolRespDTO;
import io.dynamic.threadpool.config.model.biz.threadpool.ThreadPoolSaveOrUpdateReqDTO;

import java.util.List;

/**
 * Thread Pool Service.
 */
public interface ThreadPoolService {

    /**
     * 分页查询线程池
     *
     * @param reqDTO
     * @return
     */
    IPage<ThreadPoolRespDTO> queryThreadPoolPage(ThreadPoolQueryReqDTO reqDTO);

    /**
     * 查询线程池配置
     *
     * @param reqDTO
     * @return
     */
    ThreadPoolRespDTO getThreadPool(ThreadPoolQueryReqDTO reqDTO);

    /**
     * 新增或修改线程池配置
     *
     * @param identify
     * @param reqDTO
     */
    void saveOrUpdateThreadPoolConfig(String identify, ThreadPoolSaveOrUpdateReqDTO reqDTO);

    /**
     * 根据 ItemId 获取线程池配置
     *
     * @param itemId
     * @return
     */
    List<ThreadPoolRespDTO> getThreadPoolByItemId(String itemId);

    /**
     * Delete pool.
     *
     * @param reqDTO
     */
    void deletePool(ThreadPoolDelReqDTO reqDTO);

    /**
     * Alarm enable.
     *
     * @param id
     * @param isAlarm
     */
    void alarmEnable(String id, Integer isAlarm);

}
