package com.eceakin.todoAppSpring.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"com.eceakin.todoAppSpring.application.concretes", "com.eceakin.todoAppSpring.application.services"})
public class ServiceConfiguration {
    // This configuration ensures proper scanning of manager components
    // and their automatic wiring with service interfaces
}
