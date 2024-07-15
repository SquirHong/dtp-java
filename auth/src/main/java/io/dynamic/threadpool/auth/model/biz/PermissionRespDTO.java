package io.dynamic.threadpool.auth.model.biz;

import lombok.Data;

/**
 * Permission resp dto.
 */
@Data
public class PermissionRespDTO {

    /**
     * role
     */
    private String role;

    /**
     * source
     */
    private String resource;

    /**
     * action
     */
    private String action;

}
