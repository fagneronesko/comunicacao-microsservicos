package br.com.comunicacaomicrosservicos.productapi.modules.category.controller;

import br.com.comunicacaomicrosservicos.productapi.modules.category.dto.CategoryRequest;
import br.com.comunicacaomicrosservicos.productapi.modules.category.dto.CategoryResponse;
import br.com.comunicacaomicrosservicos.productapi.modules.category.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    private CategoryService service;

    @PostMapping
    public CategoryResponse save(@RequestBody CategoryRequest request) {
        return service.save(request);
    }

    @GetMapping
    public List<CategoryResponse> findAll() {
        return service.findAll();
    }

    @GetMapping("{id}")
    public CategoryResponse findById(@PathVariable Integer id) {
        return CategoryResponse.of(service.findById(id));
    }

    @GetMapping("description/{description}")
    public List<CategoryResponse> findByDescription(@PathVariable String description) {
        return service.findByDescription(description);
    }
}
