package com.upc.appecotech;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class AppEcoTechApplication {

    public static void main(String[] args) {
        SpringApplication.run(AppEcoTechApplication.class, args);
    }

}
