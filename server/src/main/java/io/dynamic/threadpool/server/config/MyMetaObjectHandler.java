package io.dynamic.threadpool.server.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import io.dynamic.threadpool.server.enums.DelEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Meta Object Handler.
 * 元数据处理
 */
@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "gmtCreate", Date.class, new Date());
        this.strictInsertFill(metaObject, "gmtModified", Date.class, new Date());

        this.strictInsertFill(metaObject, "delFlag", Integer.class, DelEnum.NORMAL.getIntCode());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "gmtModified", Date.class, new Date());
    }
}
