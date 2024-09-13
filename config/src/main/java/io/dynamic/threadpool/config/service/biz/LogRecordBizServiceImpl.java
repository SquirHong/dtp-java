package io.dynamic.threadpool.config.service.biz;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.dynamic.threadpool.config.mapper.LogRecordMapper;
import io.dynamic.threadpool.config.model.biz.log.LogRecordQueryReqDTO;
import io.dynamic.threadpool.config.model.biz.log.LogRecordRespDTO;
import io.dynamic.threadpool.config.toolkit.BeanUtil;
import io.dynamic.threadpool.logrecord.model.LogRecordInfo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 操作日志.
 */
@Service
@AllArgsConstructor
public class LogRecordBizServiceImpl implements LogRecordBizService {

    private final LogRecordMapper logRecordMapper;

    @Override
    public IPage<LogRecordRespDTO> queryPage(LogRecordQueryReqDTO pageQuery) {
        LambdaQueryWrapper<LogRecordInfo> queryWrapper = Wrappers.lambdaQuery(LogRecordInfo.class)
                .eq(StrUtil.isNotBlank(pageQuery.getBizNo()), LogRecordInfo::getBizNo, pageQuery.getBizNo())
                .eq(StrUtil.isNotBlank(pageQuery.getCategory()), LogRecordInfo::getCategory, pageQuery.getCategory())
                .eq(StrUtil.isNotBlank(pageQuery.getOperator()), LogRecordInfo::getOperator, pageQuery.getOperator())
                .orderByDesc(LogRecordInfo::getCreateTime);
        IPage<LogRecordInfo> selectPage = logRecordMapper.selectPage(pageQuery, queryWrapper);
        return selectPage.convert(each -> BeanUtil.convert(each, LogRecordRespDTO.class));
    }

}
