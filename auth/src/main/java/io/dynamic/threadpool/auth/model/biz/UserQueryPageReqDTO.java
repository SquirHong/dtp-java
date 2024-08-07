package io.dynamic.threadpool.auth.model.biz;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

/**
 * User query page.
 */
@Data
public class UserQueryPageReqDTO extends Page {

    /**
     * userName
     */
    private String userName;

}
