package com.example.vacationcalculator.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger/OpenAPI configuration for API documentation.
 * 
 * <p>Configures SpringDoc OpenAPI to generate interactive API documentation
 * accessible at /swagger-ui.html</p>
 */
@Configuration
public class SwaggerConfig {

    /**
     * Configures OpenAPI documentation metadata.
     * 
     * @return OpenAPI configuration
     */
    @Bean
    public OpenAPI vacationCalculatorOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Vacation Pay Calculator API")
                        .description("REST API for calculating vacation pay according to Russian labor law conventions. " +
                                "Supports calculation based on vacation days or date ranges with automatic holiday exclusion.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Vacation Calculator Team")
                                .email("support@example.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")));
    }
}
