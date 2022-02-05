package br.com.comunicacaomicrosservicos.productapi.modules.category.service;

import br.com.comunicacaomicrosservicos.productapi.config.exception.SucessResponse;
import br.com.comunicacaomicrosservicos.productapi.modules.category.dto.CategoryRequest;
import br.com.comunicacaomicrosservicos.productapi.modules.category.dto.CategoryResponse;
import br.com.comunicacaomicrosservicos.productapi.modules.category.model.Category;
import br.com.comunicacaomicrosservicos.productapi.modules.category.repository.CategoryRepository;
import br.com.comunicacaomicrosservicos.productapi.modules.category.utils.CategoryExceptions;
import br.com.comunicacaomicrosservicos.productapi.modules.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static br.com.comunicacaomicrosservicos.productapi.modules.category.utils.CategoryExceptions.*;
import static br.com.comunicacaomicrosservicos.productapi.modules.supplier.utils.SupplierConstants.DELETE_SUCCCESS;
import static java.util.Objects.isNull;
import static org.springframework.util.ObjectUtils.isEmpty;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository repository;
    @Autowired
    private ProductService productService;

    public CategoryResponse save(CategoryRequest request) {
        validateName(request);
        return CategoryResponse.of(repository.save(Category.of(request)));
    }

    public CategoryResponse update(CategoryRequest request, Integer id) {
        validateName(request);
        validateId(id);
        var category = Category.of(request);
        category.setId(id);

        return CategoryResponse.of(repository.save(category));
    }

    public Category findById(Integer id) {
        validateId(id);
        return repository.findById(id)
            .orElseThrow(() -> EX_ID_NOT_EXISTS);
    }

    public List<CategoryResponse> findAll() {
        return repository.findAll()
            .stream()
            .map(CategoryResponse::of)
            .collect(Collectors.toList());
    }

    public List<CategoryResponse> findByDescription(String description) {
        if (isEmpty(description)) {
            throw EX_DESCRIPTION_NOT_INFORMED;
        }

        return repository.findByDescriptionIgnoreCaseContaining(description)
            .stream()
            .map(CategoryResponse::of)
            .collect(Collectors.toList());
    }

    public SucessResponse delete(Integer id) {
        validateId(id);
        if (productService.existsByCategoryId(id)) {
            throw EX_ALREADY_DEFINED_PRODUCT_BY_ID;
        }
        repository.deleteById(id);

        return SucessResponse.create(DELETE_SUCCCESS);
    }

    private void validateId(Integer id) {
        if (isNull(id)) {
            throw EX_ID_NOT_INFORMED;
        }
    }

    private void validateName(CategoryRequest request) {
        if (isEmpty(request.getDescription())) {
            throw CategoryExceptions.EX_DESCRIPTION_NOT_INFORMED;
        }
    }
}
