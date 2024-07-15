package io.dynamic.threadpool.auth.model.biz;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

/**
 * User query page.
 */
@Data
public class UserQueryPageReqDTO extends Page {

    public UserQueryPageReqDTO(long current, long size) {
        super(current, size);
    }

}
