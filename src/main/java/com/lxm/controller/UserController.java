package com.lxm.controller;


import com.lxm.common.lang.Result;
import com.lxm.entity.User;
import com.lxm.service.UserService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lxm
 * @since 2021-01-23
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @RequiresAuthentication
    @GetMapping("/index")
    public Object index() {
        User user = userService.getById(1L);
        return Result.success(200, "操作成功", user);
    }


    @PostMapping("/save")
    public Object save(@Validated @RequestBody User user) {
        return Result.success(user);
    }

}
