package com.tfswx.my_send;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MySendApplication {

    public static void main(String[] args) {
        SpringApplication.run(MySendApplication.class, args);
        System.out.println("文书卷宗同步源端启动成功");
    }
}
