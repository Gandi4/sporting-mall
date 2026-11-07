package com.example.sporting.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 1. 当 URL 匹配 /uploads/** 时
        // 2. 去本地硬盘 D:/code/sporting-vue/public/uploads/ 目录下找文件
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:D:/code/sporting-vue/public/uploads/");
    }
}
