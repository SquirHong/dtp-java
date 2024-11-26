package io.dynamic.threadpool.config.service.biz;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import io.dynamic.threadpool.common.toolkit.GroupKey;
import io.dynamic.threadpool.common.web.exception.ServiceException;
import io.dynamic.threadpool.config.enums.DelEnum;
import io.dynamic.threadpool.config.mapper.NotifyInfoMapper;
import io.dynamic.threadpool.config.model.NotifyInfo;
import io.dynamic.threadpool.config.model.biz.notify.NotifyListRespDTO;
import io.dynamic.threadpool.config.model.biz.notify.NotifyModifyReqDTO;
import io.dynamic.threadpool.config.model.biz.notify.NotifyQueryReqDTO;
import io.dynamic.threadpool.config.model.biz.notify.NotifyRespDTO;
import io.dynamic.threadpool.config.toolkit.BeanUtil;
import lombok.AllArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 通知管理.
 */
@Service
@AllArgsConstructor
public class NotifyServiceImpl implements NotifyService {

    private final NotifyInfoMapper notifyInfoMapper;

    @Override
    public List<NotifyListRespDTO> listNotifyConfig(NotifyQueryReqDTO reqDTO) {
        System.out.println("reqDTO.getGroupKeys() = " + reqDTO.getGroupKeys());
        List<NotifyListRespDTO> notifyListRespList = Lists.newArrayList();
        reqDTO.getGroupKeys().forEach(each -> {
            String[] parseKey = GroupKey.parseKey(each);
            // 包含type = 1 or 2 ，CONFIG or ALARM
            for (String s : parseKey) {
                System.out.println(s);
            }
            LambdaQueryWrapper<NotifyInfo> queryWrapper = Wrappers.lambdaQuery(NotifyInfo.class)
                    .eq(NotifyInfo::getTenantId, parseKey[2])
                    .eq(NotifyInfo::getItemId, parseKey[1])
                    .eq(NotifyInfo::getTpId, parseKey[0])
                    .eq(NotifyInfo::getEnable, DelEnum.NORMAL);
            List<NotifyInfo> Infos = notifyInfoMapper.selectList(queryWrapper);
            List<NotifyInfo> notifyInfos = new ArrayList<>();
            List<NotifyInfo> alarmInfos = new ArrayList<>();
            Infos.forEach(Info -> {
                if (Info.getType().equals("CONFIG")) {
                    notifyInfos.add(Info);
                } else if (Info.getType().equals("ALARM")) {
                    alarmInfos.add(Info);
                }
            });

            if (notifyInfos.size() > 0) {
                notifyListRespList.add(new NotifyListRespDTO(StrUtil.builder(parseKey[0], "+", "CONFIG").toString(), notifyInfos));
            }
            if (alarmInfos.size() > 0) {
                notifyListRespList.add(new NotifyListRespDTO(StrUtil.builder(parseKey[0], "+", "ALARM").toString(), alarmInfos));
            }

        });
        return notifyListRespList;
    }

    @Override
    public IPage<NotifyRespDTO> queryPage(NotifyQueryReqDTO reqDTO) {
        LambdaQueryWrapper<NotifyInfo> queryWrapper = Wrappers.lambdaQuery(NotifyInfo.class)
                .eq(StrUtil.isNotBlank(reqDTO.getTenantId()), NotifyInfo::getTenantId, reqDTO.getTenantId())
                .eq(StrUtil.isNotBlank(reqDTO.getItemId()), NotifyInfo::getItemId, reqDTO.getItemId())
                .eq(StrUtil.isNotBlank(reqDTO.getTpId()), NotifyInfo::getTpId, reqDTO.getTpId())
                .orderByDesc(NotifyInfo::getGmtCreate);

        IPage<NotifyInfo> resultPage = notifyInfoMapper.selectPage(reqDTO, queryWrapper);
        return resultPage.convert(each -> BeanUtil.convert(each, NotifyRespDTO.class));
    }


    @Override
    public void save(NotifyModifyReqDTO reqDTO) {
        if (existNotify(reqDTO)) {
            throw new ServiceException("新增通知报警配置重复.");
        }
        notifyInfoMapper.insert(BeanUtil.convert(reqDTO, NotifyInfo.class));
    }



    @Override
    public void update(NotifyModifyReqDTO reqDTO) {
        NotifyInfo notifyInfo = BeanUtil.convert(reqDTO, NotifyInfo.class);
        LambdaUpdateWrapper<NotifyInfo> updateWrapper = Wrappers.lambdaUpdate(NotifyInfo.class)
                .eq(NotifyInfo::getId, reqDTO.getId());

        try {
            notifyInfoMapper.update(notifyInfo, updateWrapper);
        } catch (DuplicateKeyException ex) {
            throw new ServiceException("通知配置已存在");
        }
    }

    @Override
    public void delete(NotifyModifyReqDTO reqDTO) {
        LambdaUpdateWrapper<NotifyInfo> updateWrapper = Wrappers.lambdaUpdate(NotifyInfo.class)
                .eq(NotifyInfo::getId, reqDTO.getId());

        notifyInfoMapper.delete(updateWrapper);
    }

    @Override
    public void enableNotify(String id, Integer status) {
        NotifyInfo notifyInfo = new NotifyInfo();
        notifyInfo.setId(Long.parseLong(id));
        notifyInfo.setEnable(status);
        notifyInfoMapper.updateById(notifyInfo);
    }


    private boolean existNotify(NotifyModifyReqDTO reqDTO) {
        LambdaQueryWrapper<NotifyInfo> queryWrapper = Wrappers.lambdaQuery(NotifyInfo.class)
                .eq(NotifyInfo::getTenantId, reqDTO.getTenantId())
                .eq(NotifyInfo::getItemId, reqDTO.getItemId())
                .eq(NotifyInfo::getTpId, reqDTO.getTpId())
                .eq(NotifyInfo::getPlatform, reqDTO.getPlatform())
                .eq(NotifyInfo::getType, reqDTO.getType());

        List<NotifyInfo> existNotifyInfos = notifyInfoMapper.selectList(queryWrapper);
        return CollUtil.isNotEmpty(existNotifyInfos);
    }


}
