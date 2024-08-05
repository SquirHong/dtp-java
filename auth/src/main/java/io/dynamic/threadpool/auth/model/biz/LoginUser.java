package io.dynamic.threadpool.auth.model.biz;

import lombok.Data;

/**
 * Login user.
 */
@Data
public class LoginUser {

    /**
     * username
     */
    private String username;

    /**
     * password
     */
    private String password;

    /**
     * rememberMe
     */
    private Integer rememberMe;

}
