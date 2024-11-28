package io.dynamic.threadpool.config.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

/**
 * Config instance info.
 */
@Data
@TableName("inst_config")
public class ConfigInstanceInfo {

    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * tenantId
     */
    private String tenantId;

    /**
     * itemId
     */
    private String itemId;

    /**
     * tpId
     */
    private String tpId;

    /**
     * instanceId == identify
     */
    private String instanceId;

    /**
     * MD5
     */
    private String md5;

    /**
     * content
     */
    private String content;

    /**
     * gmtCreate
     */
    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

}
