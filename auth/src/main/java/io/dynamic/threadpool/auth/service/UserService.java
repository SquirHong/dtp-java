package io.dynamic.threadpool.auth.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.dynamic.threadpool.auth.model.biz.UserRespDTO;

import java.util.List;

/**
 * User service.
 */
public interface UserService {

    /**
     * 分页查询用户列表.
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    IPage<UserRespDTO> listUser(int pageNo, int pageSize);

    /**
     * 新增用户.
     *
     * @param userName
     * @param password
     */
    void addUser(String userName, String password);

    /**
     * 修改用户.
     *
     * @param userName
     * @param password
     */
    void updateUser(String userName, String password);

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
