package io.dynamic.threadpool.config.service.biz;


import com.baomidou.mybatisplus.core.metadata.IPage;
import io.dynamic.threadpool.config.model.biz.log.LogRecordQueryReqDTO;
import io.dynamic.threadpool.config.model.biz.log.LogRecordRespDTO;

/**
 * 操作日志.
 */
public interface LogRecordBizService {

    /**
     * 查询操作日志.
     *
     * @param pageQuery
     * @return
     */
    IPage<LogRecordRespDTO> queryPage(LogRecordQueryReqDTO pageQuery);

}
