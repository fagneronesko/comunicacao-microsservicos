package br.com.comunicacaomicrosservicos.productapi.modules.produto.controller;

import br.com.comunicacaomicrosservicos.productapi.modules.produto.dto.CategoryRequest;
import br.com.comunicacaomicrosservicos.productapi.modules.produto.dto.CategoryResponse;
import br.com.comunicacaomicrosservicos.productapi.modules.produto.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    private CategoryService service;

    @PostMapping
    public CategoryResponse save(@RequestBody CategoryRequest request) {
        return service.save(request);
    }
}
