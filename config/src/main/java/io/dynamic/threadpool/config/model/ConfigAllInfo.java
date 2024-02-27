package io.dynamic.threadpool.config.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.dynamic.threadpool.common.model.PoolParameter;
import lombok.Data;

import java.util.Date;

/**
 * 配置全部信息
 */
@Data
@TableName("config_info")
public class ConfigAllInfo extends ConfigInfo implements PoolParameter {

    private static final long serialVersionUID = -2417394244017463665L;

    /**
     * 简介
     */
    @JSONField(serialize = false)
    @TableField(exist = false, fill = FieldFill.UPDATE)
    private String desc;

    /**
     * 创建时间
     */
    @JSONField(serialize = false)
    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    /**
     * 修改时间
     */
    @JSONField(serialize = false)
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;

    /**
     * 是否删除
     */
    @TableLogic
    @JSONField(serialize = false)
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer delFlag;

}
