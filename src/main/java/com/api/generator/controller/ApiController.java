package com.api.generator.controller;

import com.api.generator.model.ApiRequest;
import com.api.generator.service.AIService;
import com.api.generator.service.ApiGeneratorService;
import freemarker.template.TemplateException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Path;
import java.util.zip.ZipOutputStream;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import java.nio.file.Files;
import java.util.zip.ZipEntry;


@RestController
@RequestMapping("/api/generator")
@Tag(name = "API Generator", description = "Automatic Java API")
public class ApiController {
    private final ApiGeneratorService generatorService;
    private final AIService aiService;

    public ApiController(ApiGeneratorService apiGeneratorService, AIService aiService) {
        this.generatorService = apiGeneratorService;
        this.aiService = aiService;
    }

    @PostMapping("/generate-ai")
    @Operation(summary = "Generates a Java Class based on the informed prompt")
    public ResponseEntity<String> generateClassWithAI(@RequestParam String prompt, @RequestParam String className) {
        String generatedCode = aiService.generateCodeFromPrompt(className, prompt);
        return ResponseEntity.ok(generatedCode);
    }


    @PostMapping("/generate")
    @Operation(summary = "Generates a Java Controller based on the informed entity")
    public String generate(@RequestParam String packageName, @RequestParam String entityName) {
        try {
            generatorService.generateController(packageName, entityName);
            return "Controller generated successfully!";
        } catch (IOException | TemplateException e) {
            return "Error generating controller: " + e.getMessage();
        }
    }

    @GetMapping("/simple-api")
    @Operation(summary = "Generates a Java API based on the informed entity")
    public ResponseEntity<byte[]> generateSimpleApi(
            @RequestParam String projectName,
            @RequestParam String packageName) throws IOException, TemplateException {

        Path projectDir = generatorService.generateProject(projectName, packageName);
        Path zipPath = projectDir.resolveSibling(projectName + ".zip");

        try (ZipOutputStream zs = new ZipOutputStream(Files.newOutputStream(zipPath))) {
            Files.walk(projectDir).filter(Files::isRegularFile).forEach(file -> {
                ZipEntry zipEntry = new ZipEntry(projectDir.relativize(file).toString());
                try {
                    zs.putNextEntry(zipEntry);
                    Files.copy(file, zs);
                    zs.closeEntry();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        byte[] zipContent = Files.readAllBytes(zipPath);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + projectName + ".zip")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(zipContent);
    }
}
