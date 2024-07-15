package io.dynamic.threadpool.auth.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

/**
 * Permission info.
 */
@Data
@TableName("permission")
public class PermissionInfo {

    /**
     * id
     */
    @TableId
    private Long id;

    /**
     * role
     */
    private String role;

    /**
     * resource
     */
    private String resource;

    /**
     * action
     */
    private String action;

    /**
     * gmtCreate
     */
    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    /**
     * gmtModified
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;

    /**
     * delFlag
     */
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Integer delFlag;

}
