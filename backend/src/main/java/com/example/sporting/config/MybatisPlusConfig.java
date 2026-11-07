package com.example.sporting.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis-Plus 配置类
 * 主要配置分页插件和 Mapper 接口扫描包路径
 */
@Configuration
@MapperScan("com.example.sporting.mapper")  // 修改为实际 mapper 包路径
public class MybatisPlusConfig {

    /**
     * 添加分页插件
     * 新版 MyBatis-Plus（3.4.0+）使用 MybatisPlusInterceptor
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 添加分页拦截器，指定数据库类型为 MySQL
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        // 注意：如果以后要加乐观锁、逻辑删除等其他插件，分页插件建议放在最后添加
        return interceptor;
    }
}