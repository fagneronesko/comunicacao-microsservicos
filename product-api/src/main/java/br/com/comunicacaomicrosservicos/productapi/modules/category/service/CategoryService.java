package br.com.comunicacaomicrosservicos.productapi.modules.category.service;

import br.com.comunicacaomicrosservicos.productapi.config.exception.ValidationException;
import br.com.comunicacaomicrosservicos.productapi.modules.category.dto.CategoryRequest;
import br.com.comunicacaomicrosservicos.productapi.modules.category.dto.CategoryResponse;
import br.com.comunicacaomicrosservicos.productapi.modules.category.model.Category;
import br.com.comunicacaomicrosservicos.productapi.modules.category.repository.CategoryRepository;
import br.com.comunicacaomicrosservicos.productapi.modules.category.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository repository;

    public CategoryResponse save(CategoryRequest request) {
        var category = Category.of(request);
        validateName(category);

        return CategoryResponse.of(repository.save(category));
    }

    private void validateName(Category category) {
        if (isEmpty(category.getDescription())) {
            throw new ValidationException(Constants.EX_DESCRIPTION_NOT_INFORMED);
        }
    }
}
