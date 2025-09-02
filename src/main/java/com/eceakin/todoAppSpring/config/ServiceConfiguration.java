package com.eceakin.todoAppSpring.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"com.todoapp.manager", "com.todoapp.service"})
public class ServiceConfiguration {
    // This configuration ensures proper scanning of manager components
    // and their automatic wiring with service interfaces
}
