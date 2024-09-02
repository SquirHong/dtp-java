package io.dynamic.threadpool.console.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.dynamic.threadpool.config.enums.DelEnum;
import io.dynamic.threadpool.config.mapper.ConfigInfoMapper;
import io.dynamic.threadpool.config.mapper.ItemInfoMapper;
import io.dynamic.threadpool.config.mapper.TenantInfoMapper;
import io.dynamic.threadpool.config.model.ConfigAllInfo;
import io.dynamic.threadpool.config.model.ItemInfo;
import io.dynamic.threadpool.config.model.TenantInfo;
import io.dynamic.threadpool.console.model.ChartInfo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Dashboard service impl.
 */
@Service
@AllArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final TenantInfoMapper tenantInfoMapper;

    private final ItemInfoMapper itemInfoMapper;

    private final ConfigInfoMapper configInfoMapper;

    @Override
    public ChartInfo getChartInfo() {
        Integer tenantCount = tenantInfoMapper.selectCount(Wrappers.lambdaQuery(TenantInfo.class).eq(TenantInfo::getDelFlag, DelEnum.NORMAL));
        Integer itemCount = itemInfoMapper.selectCount(Wrappers.lambdaQuery(ItemInfo.class).eq(ItemInfo::getDelFlag, DelEnum.NORMAL));
        Integer threadPoolCount = configInfoMapper.selectCount(Wrappers.lambdaQuery(ConfigAllInfo.class).eq(ConfigAllInfo::getDelFlag, DelEnum.NORMAL));

        ChartInfo chartInfo = new ChartInfo();
        chartInfo.setTenantCount(tenantCount)
                .setItemCount(itemCount)
                .setThreadPoolCount(threadPoolCount);
        return chartInfo;
    }

}
