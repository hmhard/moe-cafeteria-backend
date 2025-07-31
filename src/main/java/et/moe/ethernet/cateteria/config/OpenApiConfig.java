package et.moe.ethernet.cateteria.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("MOE Cafeteria Management System API")
                .description("""
                    A comprehensive REST API for the Ministry of Education (MOE) cafeteria management system.
                    
                    ## Features
                    - **Employee Management**: CRUD operations for employees with card ID and short code support
                    - **Meal Types & Categories**: Manage meal types (breakfast, lunch) with fasting/non-fasting categories
                    - **Support Pricing**: Automatic pricing based on employee salary eligibility
                    - **Meal Recording**: Track meal transactions with support calculations
                    - **User Authentication**: Role-based access control (Admin, Manager, Operator)
                    
                    ## Authentication
                    The API uses Basic Authentication. Default users are created on startup:
                    - **Admin**: `admin` / `admin123`
                    - **Manager**: `manager` / `manager123`
                    - **Operator**: `operator` / `operator123`
                    
                    ## Support Pricing Logic
                    Employees with salary below the configured threshold (default: 5,000 ETB) are eligible for supported pricing:
                    - **Supported Price**: Reduced price for eligible employees
                    - **Normal Price**: Full price for non-eligible employees
                    - **Support Amount**: Difference between normal and supported prices
                    """)
                .version("1.0.0")
                .contact(new Contact()
                    .name("MOE Development Team")
                    .email("dev@moe.gov.et")
                    .url("https://moe.gov.et"))
                .license(new License()
                    .name("Ministry of Education License")
                    .url("https://moe.gov.et/license")))
            .servers(List.of(
                new Server()
                    .url("http://localhost:8080/api")
                    .description("Development Server"),
                new Server()
                    .url("https://api.moe.gov.et/cafeteria")
                    .description("Production Server")))
            .addSecurityItem(new SecurityRequirement().addList("basicAuth"))
            .components(new Components()
                .addSecuritySchemes("basicAuth", new SecurityScheme()
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("basic")
                    .description("Basic Authentication")));
    }
} 