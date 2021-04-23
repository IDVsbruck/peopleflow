package com.idvsbruck.pplflw.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.idvsbruck.pplflw.api")
public class Application {

    public static void main(String[] arguments) {
        SpringApplication.run(Application.class, arguments);
    }
}
