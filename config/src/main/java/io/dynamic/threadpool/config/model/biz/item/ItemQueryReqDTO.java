package io.dynamic.threadpool.config.model.biz.item;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;


@Data
public class ItemQueryReqDTO extends Page {

    private String tenantId;

    private String itemId;

    private String itemName;

    private String owner;
}
