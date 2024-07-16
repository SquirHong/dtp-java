package io.dynamic.threadpool.auth.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.dynamic.threadpool.auth.mapper.PermissionMapper;
import io.dynamic.threadpool.auth.model.PermissionInfo;
import io.dynamic.threadpool.auth.model.biz.PermissionQueryPageReqDTO;
import io.dynamic.threadpool.auth.model.biz.PermissionRespDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Permission service impl.
 */
@Service
@AllArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    private final PermissionMapper permissionMapper;

    @Override
    public IPage<PermissionRespDTO> listPermission(int pageNo, int pageSize) {
        PermissionQueryPageReqDTO queryPage = new PermissionQueryPageReqDTO(pageNo, pageSize);
        IPage<PermissionInfo> selectPage = permissionMapper.selectPage(queryPage, null);

        return selectPage.convert(each -> BeanUtil.toBean(each, PermissionRespDTO.class));
    }

    @Override
    public void addPermission(String role, String resource, String action) {
        LambdaQueryWrapper<PermissionInfo> queryWrapper = Wrappers.lambdaQuery(PermissionInfo.class)
                .eq(PermissionInfo::getRole, role)
                .eq(PermissionInfo::getResource, resource)
                .eq(PermissionInfo::getAction, action);
        PermissionInfo existPermissionInfo = permissionMapper.selectOne(queryWrapper);
        if (existPermissionInfo != null) {
            throw new RuntimeException("权限重复");
        }

        PermissionInfo insertPermission = new PermissionInfo();
        insertPermission.setRole(role);
        insertPermission.setResource(resource);
        insertPermission.setAction(action);
        permissionMapper.insert(insertPermission);
    }

    @Override
    public void deletePermission(String role, String resource, String action) {
        LambdaUpdateWrapper<PermissionInfo> updateWrapper = Wrappers.lambdaUpdate(PermissionInfo.class)
                .eq(StrUtil.isNotBlank(role), PermissionInfo::getRole, role)
                .eq(StrUtil.isNotBlank(resource), PermissionInfo::getResource, resource)
                .eq(StrUtil.isNotBlank(action), PermissionInfo::getAction, action);

        permissionMapper.delete(updateWrapper);
    }

}
