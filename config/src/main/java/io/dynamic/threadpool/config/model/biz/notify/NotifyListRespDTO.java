package io.dynamic.threadpool.config.model.biz.notify;

import io.dynamic.threadpool.config.model.NotifyInfo;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * Alarm list resp dto.
 */
@Data
@AllArgsConstructor
public class NotifyListRespDTO {

    /**
     * 通知 Key
     */
    private String notifyKey;

    /**
     * 报警配置
     */
    private List<NotifyInfo> notifyList;

}
