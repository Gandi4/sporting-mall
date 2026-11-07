package com.example.sporting.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.sporting.entity.Manager;
import com.example.sporting.service.ManagerService;
import com.example.sporting.mapper.ManagerMapper;
import org.springframework.stereotype.Service;

/**
* @author SPORTING
* @description 针对表【manager(管理员表)】的数据库操作Service实现
* @createDate 2025-12-17 17:08:21
*/
@Service
public class ManagerServiceImpl extends ServiceImpl<ManagerMapper, Manager>
    implements ManagerService{

}




