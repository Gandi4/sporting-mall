package com.example.sporting.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.sporting.entity.Review;
import com.example.sporting.service.ReviewService;
import com.example.sporting.mapper.ReviewMapper;
import org.springframework.stereotype.Service;

/**
* @author SPORTING
* @description 针对表【review(商品评价表)】的数据库操作Service实现
* @createDate 2025-12-20 12:52:03
*/
@Service
public class ReviewServiceImpl extends ServiceImpl<ReviewMapper, Review>
    implements ReviewService{

}




