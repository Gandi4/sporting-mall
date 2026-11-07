package com.example.sporting;

import com.alibaba.druid.spring.boot3.autoconfigure.DruidDataSourceAutoConfigure;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//告诉项目mapper接口位置
@MapperScan("com.example.sporting.mapper")
@SpringBootApplication(exclude = {
		DruidDataSourceAutoConfigure.class  // 排除 Druid 的自动配置
})
public class SportingApplication {

	public static void main(String[] args) {
		SpringApplication.run(SportingApplication.class, args);
	}

}
