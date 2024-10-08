package io.dynamic.threadpool.config.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 报警管理.
 */
@Data
@TableName("notify")
public class NotifyInfo {

    /**
     * id
     */
    private Long id;

    /**
     * 租户id
     */
    private String tenantId;

    /**
     * 项目id
     */
    private String itemId;

    /**
     * 线程池id
     */
    private String tpId;

    /**
     * 通知平台
     */
    private String platform;

    // 包含type = 1 or 2 ，CONFIG or ALARM
    /**
     * 通知类型
     */
    private String type;

    /**
     * 密钥
     */
    private String secretKey;

    /**
     * 报警间隔
     */
    @TableField("`interval`")
    private Integer interval;

    /**
     * 接收者
     */
    private String receives;

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
     * 是否启用
     */
    private Integer enable;

    /**
     * 是否删除
     */
    @TableField(fill = FieldFill.INSERT)
    private Integer delFlag;

}
