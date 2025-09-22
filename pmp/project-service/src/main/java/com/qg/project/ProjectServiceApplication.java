package com.qg.project;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Description: // 类说明
 * @ClassName: ProjectServiceApplication    // 类名
 * @Author: lrt          // 创建者
 * @Date: 2025/9/20 22:19   // 时间
 * @Version: 1.0     // 版本
 */
@SpringBootApplication
@EnableFeignClients(basePackages = "com.qg.feign")
public class ProjectServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProjectServiceApplication.class, args);
    }
}
