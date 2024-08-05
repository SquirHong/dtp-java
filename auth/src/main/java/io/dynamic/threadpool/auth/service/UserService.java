package io.dynamic.threadpool.auth.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.dynamic.threadpool.auth.model.biz.UserQueryPageReqDTO;
import io.dynamic.threadpool.auth.model.biz.UserReqDTO;
import io.dynamic.threadpool.auth.model.biz.UserRespDTO;

import java.util.List;

/**
 * User service.
 */
public interface UserService {

    /**
     * 分页查询用户列表.
     *
     * @param reqDTO
     * @return
     */
    IPage<UserRespDTO> listUser(UserQueryPageReqDTO reqDTO);

    /**
     * 新增用户.
     *
     * @param reqDTO
     */
    void addUser(UserReqDTO reqDTO);

    /**
     * 修改用户.
     *
     * @param reqDTO
     */
    void updateUser(UserReqDTO reqDTO);

    /**
     * 删除用户.
     *
     * @param userName
     */
    void deleteUser(String userName);

    /**
     * 根据用户名模糊搜索.
     *
     * @param userName
     * @return
     */
    List<String> getUserLikeUsername(String userName);

}