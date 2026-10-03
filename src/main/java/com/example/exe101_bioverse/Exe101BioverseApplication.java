package com.example.exe101_bioverse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Exe101BioverseApplication {

    public static void main(String[] args) {
        com.example.exe101_bioverse.common.config.DotEnvLoader.loadIntoSystemProperties();
        SpringApplication.run(Exe101BioverseApplication.class, args);
    }

}
