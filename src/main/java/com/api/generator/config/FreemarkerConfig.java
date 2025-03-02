package com.api.generator.config;

import org.springframework.context.annotation.Bean;


import org.springframework.core.io.ClassPathResource;
import java.io.File;
import java.io.IOException;

@org.springframework.context.annotation.Configuration
public class FreemarkerConfig {

    @Bean(name = "customFreemarkerConfiguration")
    public freemarker.template.Configuration freemarkerConfiguration() throws IOException {
        freemarker.template.Configuration cfg = new freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_31);
        cfg.setDefaultEncoding("UTF-8");
        cfg.setDirectoryForTemplateLoading(new File(new ClassPathResource("templates").getURI()));
        return cfg;
    }
}
