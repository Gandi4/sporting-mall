package com.example.sporting.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.sporting.entity.ReturnRequest;
import com.example.sporting.service.ReturnRequestService;
import com.example.sporting.mapper.ReturnRequestMapper;
import org.springframework.stereotype.Service;

/**
* @author SPORTING
* @description 针对表【return_request(售后申请表)】的数据库操作Service实现
* @createDate 2025-12-21 16:46:41
*/
@Service
public class ReturnRequestServiceImpl extends ServiceImpl<ReturnRequestMapper, ReturnRequest>
    implements ReturnRequestService{

}




