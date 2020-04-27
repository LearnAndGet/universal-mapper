package com.panda;

import tk.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@MapperScan(basePackages = "com.panda.mapper")
@SpringBootApplication
public class UniversalMapperApplication {

    public static void main(String[] args) {
        SpringApplication.run(UniversalMapperApplication.class, args);
    }

}
