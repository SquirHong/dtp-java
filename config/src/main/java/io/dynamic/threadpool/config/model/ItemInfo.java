package io.dynamic.threadpool.config.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

/**
 * Item Info.
 */
@Data
@TableName("item_info")
public class ItemInfo {

    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 租户ID
     */
    private String tenantId;

    /**
     * 项目ID
     */
    private String itemId;

    /**
     * 项目名称
     */
    private String itemName;

    /**
     * 项目简介
     */
    private String itemDesc;

    /**
     * 负责人
     */
    private String owner;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;

    /**
     * 是否删除
     */
    @TableLogic
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer delFlag;

}
