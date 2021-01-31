package com.lxm.service.impl;

import com.lxm.entity.Blog;
import com.lxm.mapper.BlogMapper;
import com.lxm.service.BlogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lxm
 * @since 2021-01-23
 */
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements BlogService {

}
