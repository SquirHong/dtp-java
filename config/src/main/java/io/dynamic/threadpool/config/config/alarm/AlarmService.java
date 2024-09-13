package io.dynamic.threadpool.config.config.alarm;

import java.util.List;

/**
 * 报警管理.
 */
public interface AlarmService {

    /**
     * 查询报警配置集合.
     *
     * @param reqDTO
     * @return
     */
    List<AlarmListRespDTO> listAlarmConfig(AlarmQueryReqDTO reqDTO);

}
