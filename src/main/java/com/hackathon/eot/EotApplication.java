package com.hackathon.eot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class EotApplication {

    public static void main(String[] args) {
        SpringApplication.run(EotApplication.class, args);
    }

}
