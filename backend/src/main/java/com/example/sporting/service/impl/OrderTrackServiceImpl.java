package com.example.sporting.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.sporting.entity.OrderTrack;
import com.example.sporting.service.OrderTrackService;
import com.example.sporting.mapper.OrderTrackMapper;
import org.springframework.stereotype.Service;

/**
* @author SPORTING
* @description 针对表【order_track(物流追踪表)】的数据库操作Service实现
* @createDate 2026-04-01 13:31:43
*/
@Service
public class OrderTrackServiceImpl extends ServiceImpl<OrderTrackMapper, OrderTrack>
    implements OrderTrackService{

}




