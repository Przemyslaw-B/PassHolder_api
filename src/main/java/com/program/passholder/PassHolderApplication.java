package com.program.passholder;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
@ConfigurationPropertiesScan
public class PassHolderApplication implements CommandLineRunner {

    public static void main(String[] args) {SpringApplication.run(PassHolderApplication.class, args);}

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Running..");
    }
}
