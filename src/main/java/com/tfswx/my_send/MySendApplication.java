package com.tfswx.my_send;

import com.tfswx.my_send.utils.KillPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MySendApplication {

    public static void main(String[] args) {
        //检查程序端口占用，如果被占用杀掉进程
        new KillPort().check(9527);
        SpringApplication.run(MySendApplication.class, args);
        System.out.println("文书卷宗同步源端启动成功");
    }


}
