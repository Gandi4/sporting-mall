package com.example.sporting.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.sporting.entity.Category;
import com.example.sporting.service.CategoryService;
import com.example.sporting.mapper.CategoryMapper;
import org.springframework.stereotype.Service;

/**
* @author SPORTING
* @description 针对表【category(商品分类表)】的数据库操作Service实现
* @createDate 2025-12-20 16:15:54
*/
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
    implements CategoryService{

}




