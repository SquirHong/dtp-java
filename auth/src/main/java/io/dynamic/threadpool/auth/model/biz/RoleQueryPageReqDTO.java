package io.dynamic.threadpool.auth.model.biz;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

/**
 * Role query page.
 */
@Data
public class RoleQueryPageReqDTO extends Page {

    public RoleQueryPageReqDTO(long current, long size) {
        super(current, size);
    }

}
