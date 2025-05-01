package fr.upsaclay.easychair;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EasyChairApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(EasyChairApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

    }
}
