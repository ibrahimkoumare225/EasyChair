package fr.upsaclay.easychair;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EasyChairApplication {

    public static void main(String[] args) {
        SpringApplication.run(EasyChairApplication.class, args);
    }


}

