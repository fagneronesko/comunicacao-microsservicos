package br.com.comunicacaomicrosservicos.productapi.modules.category.service;

import br.com.comunicacaomicrosservicos.productapi.modules.category.dto.CategoryRequest;
import br.com.comunicacaomicrosservicos.productapi.modules.category.dto.CategoryResponse;
import br.com.comunicacaomicrosservicos.productapi.modules.category.model.Category;
import br.com.comunicacaomicrosservicos.productapi.modules.category.repository.CategoryRepository;
import br.com.comunicacaomicrosservicos.productapi.modules.category.utils.CategoryExceptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository repository;

    public CategoryResponse save(CategoryRequest request) {
        validateName(request);
        return CategoryResponse.of(repository.save(Category.of(request)));
    }

    public Category findById(Integer id) {
        return repository.findById(id)
            .orElseThrow(() -> CategoryExceptions.EX_ID_NOT_EXISTS);
    }

    private void validateName(CategoryRequest request) {
        if (isEmpty(request.getDescription())) {
            throw CategoryExceptions.EX_DESCRIPTION_NOT_INFORMED;
        }
    }
}
