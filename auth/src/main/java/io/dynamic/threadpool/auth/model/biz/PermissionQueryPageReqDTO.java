package io.dynamic.threadpool.auth.model.biz;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

/**
 * Permission query page.
 */
@Data
public class PermissionQueryPageReqDTO extends Page {

    public PermissionQueryPageReqDTO(long current, long size) {
        super(current, size);
    }

}
