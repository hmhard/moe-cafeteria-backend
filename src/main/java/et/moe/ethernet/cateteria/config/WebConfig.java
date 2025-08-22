package et.moe.ethernet.cateteria.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Serve files under /api/uploads/** from the local uploads/ directory
        registry.addResourceHandler("/api/uploads/**")
                .addResourceLocations("file:uploads/");
    }
} 