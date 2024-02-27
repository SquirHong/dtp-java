package io.dynamic.threadpool.config.model.biz.item;

import lombok.Data;

import java.util.Date;

/**
 * 项目出参
 */
@Data
public class ItemRespDTO {

    private Integer id;

    private String tenantId;

    private String itemId;

    private String itemName;

    private String itemDesc;

    private String owner;

    private Date gmtCreate;

    private Date gmtModified;
}
