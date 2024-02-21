package io.dynamic.threadpool.server.model.biz.item;

import lombok.Data;


@Data
public class ItemSaveReqDTO {

    private String tenantId;

    private String itemId;

    private String itemName;

    private String itemDesc;

    private String owner;

}
