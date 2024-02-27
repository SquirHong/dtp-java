package io.dynamic.threadpool.config.model.biz.item;

import lombok.Data;


@Data
public class ItemUpdateReqDTO {

    private String namespace;

    private String itemId;

    private String itemName;

    private String itemDesc;

    private String owner;

}
