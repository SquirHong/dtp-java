package io.dynamic.threadpool.auth.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.dynamic.threadpool.auth.mapper.UserMapper;
import io.dynamic.threadpool.auth.model.UserInfo;
import io.dynamic.threadpool.auth.model.biz.UserQueryPageReqDTO;
import io.dynamic.threadpool.auth.model.biz.UserReqDTO;
import io.dynamic.threadpool.auth.model.biz.UserRespDTO;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

/**
 * User service impl.
 */
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    private final RoleService roleService;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

//    @PostConstruct
//    public void init() {
//        UserInfo admin = userMapper.selectById(1);
//        if (admin == null) {
//            UserInfo userInfo = new UserInfo();
//            userInfo.setUserName("admin");
//            userInfo.setRole("ROLE_ADMIN");
//            userInfo.setPassword(bCryptPasswordEncoder.encode("618020Hjs"));
//            int insert = userMapper.insert(userInfo);
//            if (insert > 0) {
//                System.out.println("初始化 admin 用户成功");
//            }else {
//                System.out.println("初始化 admin 用户失败");
//            }
//
//        }
//    }

    @Override
    public IPage<UserRespDTO> listUser(UserQueryPageReqDTO reqDTO) {
        IPage<UserInfo> selectPage = userMapper.selectPage(reqDTO, null);

        return selectPage.convert(each -> BeanUtil.toBean(each, UserRespDTO.class));
    }

    @Override
    public void addUser(UserReqDTO reqDTO) {
        LambdaQueryWrapper<UserInfo> queryWrapper = Wrappers.lambdaQuery(UserInfo.class)
                .eq(UserInfo::getUserName, reqDTO.getUserName());
        UserInfo existUserInfo = userMapper.selectOne(queryWrapper);
        if (existUserInfo != null) {
            throw new RuntimeException("用户名重复");
        }

        reqDTO.setPassword(bCryptPasswordEncoder.encode(reqDTO.getPassword()));
        UserInfo insertUser = BeanUtil.toBean(reqDTO, UserInfo.class);
        userMapper.insert(insertUser);
    }

    @Override
    public void updateUser(UserReqDTO reqDTO) {
        if (StrUtil.isNotBlank(reqDTO.getPassword())) {
            reqDTO.setPassword(bCryptPasswordEncoder.encode(reqDTO.getPassword()));
        }
        UserInfo updateUser = BeanUtil.toBean(reqDTO, UserInfo.class);

        LambdaUpdateWrapper<UserInfo> updateWrapper = Wrappers.lambdaUpdate(UserInfo.class)
                .eq(UserInfo::getUserName, reqDTO.getUserName());
        userMapper.update(updateUser, updateWrapper);
    }

    @Override
    public void deleteUser(String userName) {
        LambdaUpdateWrapper<UserInfo> updateWrapper = Wrappers.lambdaUpdate(UserInfo.class)
                .eq(UserInfo::getUserName, userName);
        userMapper.delete(updateWrapper);
        // roleService.deleteRole("", userName);
    }

    @Override
    public List<String> getUserLikeUsername(String userName) {
        LambdaQueryWrapper<UserInfo> queryWrapper = Wrappers.lambdaQuery(UserInfo.class)
                .like(UserInfo::getUserName, userName)
                .select(UserInfo::getUserName);

        List<UserInfo> userInfos = userMapper.selectList(queryWrapper);
        List<String> userNames = userInfos.stream().map(UserInfo::getUserName).collect(Collectors.toList());

        return userNames;
    }

}
