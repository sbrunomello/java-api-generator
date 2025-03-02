package ${packageName}.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Tag(name = "API Generator: ${artifactName}", description = "Automatic Java API")
public class SimpleController {

@GetMapping("/hello")
@Operation(summary = "hello world!")
public String hello() {
return "Hello, World!";
}
}
