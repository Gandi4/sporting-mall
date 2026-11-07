package com.example.sporting.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.sporting.entity.Favorite;
import com.example.sporting.service.FavoriteService;
import com.example.sporting.mapper.FavoriteMapper;
import org.springframework.stereotype.Service;

/**
* @author SPORTING
* @description 针对表【favorite(商品收藏表)】的数据库操作Service实现
* @createDate 2026-04-02 11:30:51
*/
@Service
public class FavoriteServiceImpl extends ServiceImpl<FavoriteMapper, Favorite>
    implements FavoriteService{

}




