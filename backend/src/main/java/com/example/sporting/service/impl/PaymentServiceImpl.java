package com.example.sporting.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.sporting.entity.Payment;
import com.example.sporting.service.PaymentService;
import com.example.sporting.mapper.PaymentMapper;
import org.springframework.stereotype.Service;

/**
* @author SPORTING
* @description 针对表【payment(支付记录表)】的数据库操作Service实现
* @createDate 2025-12-20 12:52:17
*/
@Service
public class PaymentServiceImpl extends ServiceImpl<PaymentMapper, Payment>
    implements PaymentService{

}




