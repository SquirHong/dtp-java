package io.dynamic.threadpool.server.service.biz;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.dynamic.threadpool.server.model.biz.item.ItemQueryReqDTO;
import io.dynamic.threadpool.server.model.biz.item.ItemRespDTO;
import io.dynamic.threadpool.server.model.biz.item.ItemSaveReqDTO;
import io.dynamic.threadpool.server.model.biz.item.ItemUpdateReqDTO;

import java.util.List;

/**
 * Item Service.
 */
public interface ItemService {

    /**
     * 分页查询
     *
     * @param reqDTO
     * @return
     */
    IPage<ItemRespDTO> queryItemPage(ItemQueryReqDTO reqDTO);


    /**
     * 根据 Id 获取项目
     *
     * @param namespace
     * @param itemId
     * @return
     */
    ItemRespDTO queryItemById(String namespace, String itemId);

    /**
     * 查询项目
     *
     * @param reqDTO
     * @return
     */
    List<ItemRespDTO> queryItem(ItemQueryReqDTO reqDTO);

    /**
     * 新增项目
     *
     * @param reqDTO
     */
    void saveItem(ItemSaveReqDTO reqDTO);

    /**
     * 修改项目
     *
     * @param reqDTO
     */
    void updateItem(ItemUpdateReqDTO reqDTO);

    /**
     * 删除项目
     *
     * @param namespace
     * @param itemId
     */
    void deleteItem(String namespace, String itemId);

}
