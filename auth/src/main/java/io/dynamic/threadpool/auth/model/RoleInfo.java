package io.dynamic.threadpool.auth.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

/**
 * Role info.
 */
@Data
@TableName("role")
public class RoleInfo {

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
     * userName
     */
    private String userName;

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
