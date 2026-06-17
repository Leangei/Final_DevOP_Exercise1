package com.example.demo.config;

import com.example.demo.model.Template;
import com.example.demo.repository.TemplateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final TemplateRepository templateRepository;

    @Override
    public void run(String... args) {
        if (templateRepository.count() > 0) {
            log.info("Templates already exist, skipping data initialization");
            return;
        }

        Template defaultTemplate = Template.builder()
                .code("DEFAULT")
                .name("Default Blue Theme")
                .organizationName("Institute of Technology of Cambodia")
                .layout("VERTICAL")
                .primaryColor("#1d4ed8")
                .secondaryColor("#e0e7ff")
                .textColor("#111827")
                .tagline("Excellence through Knowledge")
                .build();

        templateRepository.save(defaultTemplate);

        Template darkTemplate = Template.builder()
                .code("DARK")
                .name("Dark Elegance Theme")
                .organizationName("Institute of Technology of Cambodia")
                .layout("VERTICAL")
                .primaryColor("#1e1b4b")
                .secondaryColor("#c7d2fe")
                .textColor("#f8fafc")
                .tagline("Innovation & Integrity")
                .build();

        templateRepository.save(darkTemplate);

        Template modernTemplate = Template.builder()
                .code("MODERN")
                .name("Modern Green Theme")
                .organizationName("Institute of Technology of Cambodia")
                .layout("HORIZONTAL")
                .primaryColor("#065f46")
                .secondaryColor("#d1fae5")
                .textColor("#111827")
                .tagline("Shaping Future Leaders")
                .build();

        templateRepository.save(modernTemplate);

        log.info("Created {} default templates", 3);
    }
}