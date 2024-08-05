package io.dynamic.threadpool.console.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.dynamic.threadpool.auth.model.biz.UserQueryPageReqDTO;
import io.dynamic.threadpool.auth.model.biz.UserReqDTO;
import io.dynamic.threadpool.auth.model.biz.UserRespDTO;
import io.dynamic.threadpool.auth.service.UserService;
import io.dynamic.threadpool.common.constant.Constants;
import io.dynamic.threadpool.common.web.base.Result;
import io.dynamic.threadpool.common.web.base.Results;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * User controller.
 */
@RestController
@AllArgsConstructor
@RequestMapping(Constants.BASE_PATH + "/auth/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/page")
    public Result<IPage<UserRespDTO>> listUser(@RequestBody UserQueryPageReqDTO reqDTO) {
        IPage<UserRespDTO> resultUserPage = userService.listUser(reqDTO);
        return Results.success(resultUserPage);
    }

    @PostMapping("/add")
    public Result<Void> addUser(@RequestBody UserReqDTO reqDTO) {
        userService.addUser(reqDTO);
        return Results.success();
    }

    @PutMapping("/update")
    public Result<Void> updateUser(@RequestBody UserReqDTO reqDTO) {
        userService.updateUser(reqDTO);
        return Results.success();
    }

    @DeleteMapping("/remove/{userName}")
    public Result<Void> deleteUser(@PathVariable("userName") String userName) {
        userService.deleteUser(userName);
        return Results.success();
    }

    @GetMapping("/search/{userName}")
    public Result<List<String>> searchUsersLikeUserName(@PathVariable("userName") String userName) {
        List<String> resultUserNames = userService.getUserLikeUsername(userName);
        return Results.success(resultUserNames);
    }

}
