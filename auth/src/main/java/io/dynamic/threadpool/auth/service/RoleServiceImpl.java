package io.dynamic.threadpool.auth.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.dynamic.threadpool.auth.mapper.RoleMapper;
import io.dynamic.threadpool.auth.model.RoleInfo;
import io.dynamic.threadpool.auth.model.biz.RoleQueryPageReqDTO;
import io.dynamic.threadpool.auth.model.biz.RoleRespDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Role service impl.
 */
@Service
@AllArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleMapper roleMapper;

    private final PermissionService permissionService;

    @Override
    public IPage<RoleRespDTO> listRole(int pageNo, int pageSize) {
        RoleQueryPageReqDTO queryPage = new RoleQueryPageReqDTO(pageNo, pageSize);
        IPage<RoleInfo> selectPage = roleMapper.selectPage(queryPage, null);

        return selectPage.convert(each -> BeanUtil.toBean(each, RoleRespDTO.class));
    }

    @Override
    public void addRole(String role, String userName) {
        LambdaQueryWrapper<RoleInfo> queryWrapper = Wrappers.lambdaQuery(RoleInfo.class)
                .eq(RoleInfo::getRole, role);
        RoleInfo roleInfo = roleMapper.selectOne(queryWrapper);
        if (roleInfo != null) {
            throw new RuntimeException("角色名重复");
        }

        RoleInfo insertRole = new RoleInfo();
        insertRole.setRole(role);
        insertRole.setUserName(userName);
        roleMapper.insert(insertRole);
    }

    @Override
    public void deleteRole(String role, String userName) {
        if (StrUtil.isBlank(role) && StrUtil.isBlank(userName)) {
            throw new RuntimeException("权限和用户名不能同时为空");
        }
        List<String> roleStrList = CollUtil.toList(role);
        if (StrUtil.isBlank(role)) {
            LambdaQueryWrapper<RoleInfo> queryWrapper = Wrappers.lambdaQuery(RoleInfo.class).eq(RoleInfo::getUserName, userName);
            roleStrList = roleMapper.selectList(queryWrapper).stream().map(RoleInfo::getRole).collect(Collectors.toList());
        }

        LambdaQueryWrapper<RoleInfo> updateWrapper = Wrappers.lambdaQuery(RoleInfo.class)
                .eq(RoleInfo::getRole, role)
                .eq(RoleInfo::getUserName, userName);
        roleMapper.delete(updateWrapper);

        roleStrList.forEach(each -> permissionService.deletePermission(each, "", ""));
    }

    @Override
    public List<String> getRoleLike(String role) {
        LambdaQueryWrapper<RoleInfo> queryWrapper = Wrappers.lambdaQuery(RoleInfo.class)
                .like(RoleInfo::getRole, role)
                .select(RoleInfo::getRole);

        List<RoleInfo> roleInfos = roleMapper.selectList(queryWrapper);
        List<String> roleNames = roleInfos.stream().map(RoleInfo::getRole).collect(Collectors.toList());

        return roleNames;
    }

}
