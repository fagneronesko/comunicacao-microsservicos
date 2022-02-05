package br.com.comunicacaomicrosservicos.productapi.modules.supplier.controller;

import br.com.comunicacaomicrosservicos.productapi.modules.category.dto.CategoryResponse;
import br.com.comunicacaomicrosservicos.productapi.modules.supplier.dto.SupplierRequest;
import br.com.comunicacaomicrosservicos.productapi.modules.supplier.dto.SupplierResponse;
import br.com.comunicacaomicrosservicos.productapi.modules.supplier.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/supplier")
public class SupplierController {

    @Autowired
    private SupplierService service;

    @PostMapping
    public SupplierResponse save(@RequestBody SupplierRequest request) {
        return service.save(request);
    }

    @GetMapping
    public List<SupplierResponse> findAll() {
        return service.findAll();
    }

    @GetMapping("{id}")
    public SupplierResponse findById(@PathVariable Integer id) {
        return SupplierResponse.of(service.findById(id));
    }

    @GetMapping("name/{name}")
    public List<SupplierResponse> findByName(@PathVariable String name) {
        return service.findByName(name);
    }
}
