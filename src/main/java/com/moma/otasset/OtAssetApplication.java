package com.moma.otasset;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@Slf4j
@SpringBootApplication
@MapperScan(basePackages = "com.moma.otasset.dao.mapper")
public class OtAssetApplication {

    public static void main(String[] args) {
        SpringApplication.run(OtAssetApplication.class, args);
    }

}
