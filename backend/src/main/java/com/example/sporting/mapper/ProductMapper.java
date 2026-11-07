package com.example.sporting.mapper;

import com.example.sporting.entity.Product;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author SPORTING
* @description 针对表【product(商品表)】的数据库操作Mapper
* @createDate 2025-12-17 17:23:26
* @Entity com.example.sporting.entity.Product
*/
@Mapper
public interface ProductMapper extends BaseMapper<Product> {

}




