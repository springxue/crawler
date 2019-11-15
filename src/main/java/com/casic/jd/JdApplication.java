package com.casic.jd;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.casic.jd.dao")
@EnableScheduling
public class JdApplication {
    public static void main(String[] args) {
        SpringApplication.run(JdApplication.class, args);
    }

}
