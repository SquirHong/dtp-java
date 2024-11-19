package io.dynamic.threadpool.config.service.biz;


import com.baomidou.mybatisplus.core.metadata.IPage;
import io.dynamic.threadpool.config.model.biz.notify.NotifyListRespDTO;
import io.dynamic.threadpool.config.model.biz.notify.NotifyModifyReqDTO;
import io.dynamic.threadpool.config.model.biz.notify.NotifyQueryReqDTO;
import io.dynamic.threadpool.config.model.biz.notify.NotifyRespDTO;

import java.util.List;

/**
 * 报警管理.
 */
public interface NotifyService {

    /**
     * 查询报警配置集合.
     *
     * @param reqDTO
     * @return
     */
    List<NotifyListRespDTO> listNotifyConfig(NotifyQueryReqDTO reqDTO);

    /**
     * 分页查询.
     *
     * @param reqDTO
     * @return
     */
    IPage<NotifyRespDTO> queryPage(NotifyQueryReqDTO reqDTO);

    /**
     * 新增通知配置.
     *
     * @param reqDTO
     */
    void save(NotifyModifyReqDTO reqDTO);

    /**
     * 修改通知配置.
     *
     * @param reqDTO
     */
    void update(NotifyModifyReqDTO reqDTO);

    /**
     * 删除通知配置.
     *
     * @param reqDTO
     */
    void delete(NotifyModifyReqDTO reqDTO);


    /**
     * 启用停用通知.
     *
     * @param id
     * @param status
     */
    void enableNotify(String id, Integer status);

}
