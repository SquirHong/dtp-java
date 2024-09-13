package io.dynamic.threadpool.config.config.alarm;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.util.List;

/**
 * Alarm query req dto.
 */
@Data
public class AlarmQueryReqDTO extends Page {

    /**
     * groupKeys
     */
    private List<String> groupKeys;

}
