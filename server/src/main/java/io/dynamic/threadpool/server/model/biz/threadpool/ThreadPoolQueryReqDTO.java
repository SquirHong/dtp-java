package io.dynamic.threadpool.server.model.biz.threadpool;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

/**
 * Thread Pool Query Req DTO.
 */
@Data
public class ThreadPoolQueryReqDTO extends Page {

    private String tenantId;

    private String itemId;

    private String tpId;

    private String tpName;

}
