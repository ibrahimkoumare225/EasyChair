package fr.upsaclay.easychair;

import fr.upsaclay.easychair.service.DataInitializer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class EasyChairApplication {


    public static void main(String[] args) {
        SpringApplication.run(EasyChairApplication.class, args);
    }

    // ✅ Bonne manière d'injecter un service dans le runner
    @Bean
    CommandLineRunner init(DataInitializer dataInitializer) {
        return args -> dataInitializer.initializeData();
    }
}

