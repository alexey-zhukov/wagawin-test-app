package com.wagawin.testapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableCaching
@EnableScheduling
public class TestAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestAppApplication.class, args);
    }

}
