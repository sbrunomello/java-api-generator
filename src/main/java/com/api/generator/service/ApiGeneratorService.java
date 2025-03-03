package com.api.generator.service;

import com.api.generator.util.FileUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@Service
public class ApiGeneratorService {
    private final Configuration freemarkerConfig;

    public ApiGeneratorService(@Qualifier("customFreemarkerConfiguration") Configuration freemarkerConfig) {
        this.freemarkerConfig = freemarkerConfig;
    }

    public String generateController(String packageName, String entityName) throws IOException, TemplateException {
        Template template = freemarkerConfig.getTemplate("controllerTemplate.ftl");

        Map<String, Object> data = new HashMap<>();
        data.put("packageName", packageName);
        data.put("entityName", entityName);

        File dir = new File("generated/" + packageName.replace(".", "/") + "/controller/");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File outputFile = new File(dir, entityName + "Controller.java");
        FileWriter writer = new FileWriter(outputFile);
        template.process(data, writer);
        writer.close();

        return "Arquivo gerado com sucesso: " + outputFile.getAbsolutePath();
    }


    public Path generateProject(String projectName, String packageName) throws IOException, TemplateException {
        Path projectDir = Path.of("generated-projects", projectName);
        Files.createDirectories(projectDir.resolve("src/main/java/" + packageName.replace(".", "/") + "/controller"));
        Files.createDirectories(projectDir.resolve("src/main/resources"));

        Map<String, Object> data = new HashMap<>();
        data.put("packageName", packageName);
        data.put("artifactName", projectName);

        generateFile("pom.xml.ftl", projectDir.resolve("pom.xml"), data);
        generateFile("Application.java.ftl", projectDir.resolve("src/main/java/" + packageName.replace(".", "/") + "/Application.java"), data);
        generateFile("controller/SimpleController.java.ftl", projectDir.resolve("src/main/java/" + packageName.replace(".", "/") + "/controller/SimpleController.java"), data);
        generateFile("application.properties.ftl", projectDir.resolve("src/main/resources/application.properties"), data);

        return projectDir;
    }

    private void generateFile(String templateName, Path outputFile, Map<String, Object> data) throws IOException, TemplateException {
        Template template = freemarkerConfig.getTemplate(templateName);
        try (Writer writer = new FileWriter(outputFile.toFile())) {
            template.process(data, writer);
        }
    }
}
