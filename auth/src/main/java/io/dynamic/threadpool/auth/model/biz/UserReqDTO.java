package io.dynamic.threadpool.auth.model.biz;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

/**
 * User req dto.
 */
@Data
public class UserReqDTO extends Page {

    /**
     * userName
     */
    private String userName;

    /**
     * password
     */
    private String password;

    /**
     * role
     */
    private String role;

}
