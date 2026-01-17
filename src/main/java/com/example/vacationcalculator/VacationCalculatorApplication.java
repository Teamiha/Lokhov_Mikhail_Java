package com.example.vacationcalculator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main Spring Boot application class for Vacation Pay Calculator microservice.
 * 
 * <p>This service provides a REST API endpoint to calculate vacation pay
 * according to Russian labor law conventions.</p>
 */
@SpringBootApplication
public class VacationCalculatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(VacationCalculatorApplication.class, args);
    }
}
