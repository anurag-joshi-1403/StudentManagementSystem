package com.anurag.sms.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        // Student Images
        registry.addResourceHandler("/student-images/**")
                .addResourceLocations("file:uploads/student-images/");

        // Teacher Images
        registry.addResourceHandler("/teacher-images/**")
                .addResourceLocations("file:uploads/teacher-images/");

    }
}