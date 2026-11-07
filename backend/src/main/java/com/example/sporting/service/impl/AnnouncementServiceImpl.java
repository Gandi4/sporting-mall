package com.example.sporting.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.sporting.entity.Announcement;
import com.example.sporting.service.AnnouncementService;
import com.example.sporting.mapper.AnnouncementMapper;
import org.springframework.stereotype.Service;

/**
* @author SPORTING
* @description 针对表【announcement(系统公告表)】的数据库操作Service实现
* @createDate 2025-12-20 12:52:57
*/
@Service
public class AnnouncementServiceImpl extends ServiceImpl<AnnouncementMapper, Announcement>
    implements AnnouncementService{

}




