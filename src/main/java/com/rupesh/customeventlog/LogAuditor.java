package com.rupesh.customeventlog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ImageBanner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;

import com.rupesh.customeventlog.service.LogAuditorService;

import java.time.Duration;
import java.time.Instant;

@SpringBootApplication
public class LogAuditor implements CommandLineRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogAuditor.class);

    @Autowired
    private LogAuditorService service;

    public static void main(String... args) {
    	LOGGER.info("Application Started....");
    	SpringApplication app = new SpringApplication(LogAuditor.class);
        app.run(args);
        LOGGER.info("Application Ended....");
    }

    @Override
    public void run(String... args) {
        Instant start = Instant.now();
        service.execute(args);
        Instant end = Instant.now();
        LOGGER.info("Total time in ms", Duration.between(start, end).toMillis());
    }
}
