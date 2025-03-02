package ${packageName}.controller;

import ${packageName}.service.${entityName}Service;
import ${packageName}.model.${entityName};
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/${entityName?lower_case}")
public class ${entityName}Controller {

    private final ${entityName}Service service;

    public ${entityName}Controller(${entityName}Service service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<${entityName}>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<${entityName}> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    public ResponseEntity<${entityName}> create(@RequestBody ${entityName} entity) {
        return ResponseEntity.ok(service.create(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<${entityName}> update(@PathVariable Long id, @RequestBody ${entityName} entity) {
        return ResponseEntity.ok(service.update(id, entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
