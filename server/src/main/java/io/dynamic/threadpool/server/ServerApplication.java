package io.dynamic.threadpool.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@MapperScan(basePackages =
        {
                "io.dynamic.threadpool.config.mapper",
                "io.dynamic.threadpool.auth.mapper"
        })
@SpringBootApplication(scanBasePackages = "io.dynamic.threadpool")
public class ServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

}
