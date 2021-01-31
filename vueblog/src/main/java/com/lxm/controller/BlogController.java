package com.lxm.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lxm.common.lang.Result;
import com.lxm.entity.Blog;
import com.lxm.service.BlogService;
import com.lxm.shiro.AccountProfile;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lxm
 * @since 2021-01-23
 */
@RestController
public class BlogController {

    @Autowired
    BlogService blogService;

    @GetMapping("/blogs")
    public Result list(@RequestParam(defaultValue = "1") Integer currentPage) {
        Page<Blog> page = new Page<>(currentPage, 5);
        IPage<Blog> pageInfo = blogService.page(page, new QueryWrapper<Blog>().orderByDesc("created"));
        return Result.success(pageInfo);
    }

    @GetMapping("/blog/{id}")
    public Result detail(@PathVariable Long id) {
        Blog blog = blogService.getById(id);
        Assert.notNull(blog, "该博客已被删除");

        return Result.success(blog);
    }

    @RequiresAuthentication
    @PostMapping("/blog/edit")
    public Result edit(@Validated @RequestBody Blog blog) {

        Blog temp = null;
        AccountProfile accountProfile = (AccountProfile) (SecurityUtils.getSubject().getPrincipal());
        if (blog.getId() == null) {
            // 添加博客
            temp = new Blog();
            temp.setUserId(accountProfile.getId());
            temp.setCreated(LocalDateTime.now());
            temp.setStatus(0);
        } else {
            // 编辑博客

            // 只能编辑自己的博客
            temp = blogService.getById(blog.getId());
            Assert.isTrue(temp.getUserId().equals(accountProfile.getId()), "没有权限编辑");
        }

        BeanUtils.copyProperties(blog, temp, "id", "userId", "created", "status");
        blogService.saveOrUpdate(temp);
        return Result.success(null);
    }

    @RequiresAuthentication
    @GetMapping("/blog/delete/{id}")
    public Result delete(@PathVariable Long id) {
        AccountProfile accountProfile = (AccountProfile) (SecurityUtils.getSubject().getPrincipal());
        Blog blog = blogService.getById(id);

        Assert.notNull(blog, "博客不存在");

        Assert.isTrue(blog.getUserId().equals(accountProfile.getId()), "没有权限删除");
        blogService.removeById(blog.getId());
        return Result.success(null);
    }

}
