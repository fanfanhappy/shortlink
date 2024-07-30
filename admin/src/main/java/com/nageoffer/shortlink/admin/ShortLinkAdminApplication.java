package com.nageoffer.shortlink.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @projectName: shortlink
 * @package: com.nageoffer.shortlink.admin
 * @className: ShortLinkAdminApplication
 * @author: 姬紫衣
 * @description: TODO
 * @date: 2024/7/30 11:00
 * @version: 1.0
 */

@SpringBootApplication
/*持久层包扫描*/
@MapperScan("com.nageoffer.shortlink.admin.dao.mapper")
public class ShortLinkAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShortLinkAdminApplication.class);
    }
}
