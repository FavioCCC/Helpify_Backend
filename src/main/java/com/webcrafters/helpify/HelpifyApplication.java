package com.webcrafters.helpify;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class HelpifyApplication {

    public static void main(String[] args) {
        SpringApplication.run(HelpifyApplication.class, args);
    }

}
