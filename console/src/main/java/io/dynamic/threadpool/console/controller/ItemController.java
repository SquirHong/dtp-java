package io.dynamic.threadpool.console.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.dynamic.threadpool.common.constant.Constants;
import io.dynamic.threadpool.common.web.base.Result;
import io.dynamic.threadpool.common.web.base.Results;
import io.dynamic.threadpool.config.model.biz.item.ItemQueryReqDTO;
import io.dynamic.threadpool.config.model.biz.item.ItemRespDTO;
import io.dynamic.threadpool.config.model.biz.item.ItemSaveReqDTO;
import io.dynamic.threadpool.config.model.biz.item.ItemUpdateReqDTO;
import io.dynamic.threadpool.config.service.biz.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Item Controller.
 */
@RestController
@RequestMapping(Constants.BASE_PATH + "/item")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @PostMapping("/query/page")
    public Result<IPage<ItemRespDTO>> queryItemPage(@RequestBody ItemQueryReqDTO reqDTO) {
        return Results.success(itemService.queryItemPage(reqDTO));
    }

    @GetMapping("/query/{tenantId}/{itemId}")
    public Result queryItemById(@PathVariable("tenantId") String tenantId, @PathVariable("itemId") String itemId) {
        return Results.success(itemService.queryItemById(tenantId, itemId));
    }

    @PostMapping("/save")
    public Result saveItem(@RequestBody ItemSaveReqDTO reqDTO) {
        itemService.saveItem(reqDTO);
        return Results.success();
    }

    @PostMapping("/update")
    public Result updateItem(@RequestBody ItemUpdateReqDTO reqDTO) {
        itemService.updateItem(reqDTO);
        return Results.success();
    }

    @DeleteMapping("/delete/{tenantId}/{itemId}")
    public Result deleteItem(@PathVariable("tenantId") String tenantId, @PathVariable("itemId") String itemId) {
        itemService.deleteItem(tenantId, itemId);
        return Results.success();
    }
}
