package com.qg;

import com.qg.domain.MobileError;
import com.qg.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Description: // 类说明
 * @ClassName: CountTest    // 类名
 * @Author: lrt          // 创建者
 * @Date: 2025/8/16 15:30   // 时间
 * @Version: 1.0     // 版本
 */
@SpringBootTest
public class CountTest {


    @Autowired
    private BackendPerformanceMapper backendPerformanceMapper;

    @Autowired
    private BackendErrorMapper backendErrorMapper;

    @Autowired
    private FrontendErrorMapper frontendErrorMapper;


    @Autowired
    private FrontendPerformanceMapper frontendPerformanceMapper;

    @Autowired
    private MobileErrorMapper mobileErrorMapper;

    public void count() {
        // 统计后端错误
        int backendErrorCount = Math.toIntExact(backendErrorMapper.selectCount(null));
        System.out.println("后端错误总数: " + backendErrorCount);

        // 统计前端错误
        int frontendErrorCount = Math.toIntExact(frontendErrorMapper.selectCount(null));
        System.out.println("前端错误总数: " + frontendErrorCount);

        // 统计移动端错误
        int mobileErrorCount = Math.toIntExact(mobileErrorMapper.selectCount(null));
        System.out.println("移动端错误总数: " + mobileErrorCount);

        // 统计后端性能
        int backendPerformanceCount = Math.toIntExact(backendPerformanceMapper.selectCount(null));
        System.out.println("后端性能总数: " + backendPerformanceCount);

        // 统计前端性能
        int frontendPerformanceCount = Math.toIntExact(frontendPerformanceMapper.selectCount(null));
        System.out.println("前端性能总数: " + frontendPerformanceCount);
    }
}
