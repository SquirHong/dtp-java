package io.dynamic.threadpool.config.service.biz;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.dynamic.threadpool.config.enums.DelEnum;
import io.dynamic.threadpool.config.mapper.ConfigInfoMapper;
import io.dynamic.threadpool.config.model.ConfigAllInfo;
import io.dynamic.threadpool.config.model.biz.threadpool.ThreadPoolDelReqDTO;
import io.dynamic.threadpool.config.model.biz.threadpool.ThreadPoolQueryReqDTO;
import io.dynamic.threadpool.config.model.biz.threadpool.ThreadPoolRespDTO;
import io.dynamic.threadpool.config.model.biz.threadpool.ThreadPoolSaveOrUpdateReqDTO;
import io.dynamic.threadpool.config.toolkit.BeanUtil;
import io.dynamic.threadpool.logrecord.annotation.LogRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Thread Pool Service Impl.
 */
@Service
public class ThreadPoolServiceImpl implements ThreadPoolService {

    @Autowired
    private ConfigService configService;

    @Resource
    private ConfigInfoMapper configInfoMapper;

    @Override
    public IPage<ThreadPoolRespDTO> queryThreadPoolPage(ThreadPoolQueryReqDTO reqDTO) {
        LambdaQueryWrapper<ConfigAllInfo> wrapper = Wrappers.lambdaQuery(ConfigAllInfo.class)
                .eq(!StringUtils.isBlank(reqDTO.getTenantId()), ConfigAllInfo::getTenantId, reqDTO.getTenantId())
                .eq(!StringUtils.isBlank(reqDTO.getItemId()), ConfigAllInfo::getItemId, reqDTO.getItemId())
                .eq(!StringUtils.isBlank(reqDTO.getTpId()), ConfigAllInfo::getTpId, reqDTO.getTpId())
                .eq(ConfigAllInfo::getDelFlag, DelEnum.DELETE)
                .orderByDesc(ConfigAllInfo::getGmtModified);
        return configInfoMapper.selectPage(reqDTO, wrapper).convert(each -> BeanUtil.convert(each, ThreadPoolRespDTO.class));
    }

    @Override
    public ThreadPoolRespDTO getThreadPool(ThreadPoolQueryReqDTO reqDTO) {
        ConfigAllInfo configAllInfo = configService.findConfigAllInfo(reqDTO.getTpId(), reqDTO.getItemId(), reqDTO.getTenantId());
        return BeanUtil.convert(configAllInfo, ThreadPoolRespDTO.class);
    }

    @Override
    public void saveOrUpdateThreadPoolConfig(String identify, ThreadPoolSaveOrUpdateReqDTO reqDTO) {
        configService.insertOrUpdate(identify, BeanUtil.convert(reqDTO, ConfigAllInfo.class));
    }

    @Override
    public List<ThreadPoolRespDTO> getThreadPoolByItemId(String itemId) {
        List<ConfigAllInfo> selectList = configInfoMapper
                .selectList(
                        Wrappers.lambdaQuery(ConfigAllInfo.class).eq(ConfigAllInfo::getItemId, itemId)
                );
        return BeanUtil.convert(selectList, ThreadPoolRespDTO.class);
    }


    @LogRecord(
            bizNo = "{{#reqDTO.itemId}}_{{#reqDTO.tpId}}",
            category = "THREAD_POOL_DELETE",
            success = "删除线程池: {{#reqDTO.tpId}}",
            detail = "{{#reqDTO.toString()}}"
    )
    @Override
    public void deletePool(ThreadPoolDelReqDTO reqDTO) {
        configInfoMapper.delete(
                Wrappers.lambdaUpdate(ConfigAllInfo.class)
                        .eq(ConfigAllInfo::getTenantId, reqDTO.getTenantId())
                        .eq(ConfigAllInfo::getItemId, reqDTO.getItemId())
                        .eq(ConfigAllInfo::getTpId, reqDTO.getTpId())
        );
    }

    @Override
    public void alarmEnable(String id, Integer isAlarm) {
        ConfigAllInfo configAllInfo = configInfoMapper.selectById(id);
        configAllInfo.setIsAlarm(isAlarm);
        // TODO: 是否报警变更, 虽然通知了客户端, 但客户端并没有“处理”这个变更
        configService.insertOrUpdate(null, configAllInfo);
    }

}
