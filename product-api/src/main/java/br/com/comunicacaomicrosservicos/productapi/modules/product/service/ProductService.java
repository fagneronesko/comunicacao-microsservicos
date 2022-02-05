package br.com.comunicacaomicrosservicos.productapi.modules.product.service;

import br.com.comunicacaomicrosservicos.productapi.modules.category.service.CategoryService;
import br.com.comunicacaomicrosservicos.productapi.modules.product.dto.ProductRequest;
import br.com.comunicacaomicrosservicos.productapi.modules.product.dto.ProductResponse;
import br.com.comunicacaomicrosservicos.productapi.modules.product.model.Product;
import br.com.comunicacaomicrosservicos.productapi.modules.product.repository.ProductRepository;
import br.com.comunicacaomicrosservicos.productapi.modules.product.utils.ProductExceptions;
import br.com.comunicacaomicrosservicos.productapi.modules.supplier.dto.SupplierResponse;
import br.com.comunicacaomicrosservicos.productapi.modules.supplier.model.Supplier;
import br.com.comunicacaomicrosservicos.productapi.modules.supplier.service.SupplierService;
import br.com.comunicacaomicrosservicos.productapi.modules.supplier.utils.SupplierExceptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.springframework.util.ObjectUtils.isEmpty;

@Service
public class ProductService {

    private static final Integer ZERO = 0;

    @Autowired
    private ProductRepository repository;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private SupplierService supplierService;

    public ProductResponse save(ProductRequest request) {
        validateProductData(request);

        return ProductResponse.of(
            repository.save(
                Product.of(
                    request,
                    categoryService.findById(request.getCategoryId()),
                    supplierService.findById(request.getSupplierId()))));
    }

    public Product findById(Integer id) {
        if (isNull(id)) {
            throw ProductExceptions.EX_ID_NOT_INFORMED;
        }
        return repository.findById(id)
            .orElseThrow(() -> ProductExceptions.EX_ID_NOT_EXISTS);
    }

    public List<ProductResponse> findAll() {
        return repository.findAll()
            .stream()
            .map(ProductResponse::of)
            .collect(Collectors.toList());
    }

    public List<ProductResponse> findByName(String name) {
        if (isEmpty(name)) {
            throw ProductExceptions.EX_NAME_NOT_INFORMED;
        }

        return repository.findByNameIgnoreCaseContaining(name)
            .stream()
            .map(ProductResponse::of)
            .collect(Collectors.toList());
    }

    public List<ProductResponse> findByCategoryId(Integer categoryId) {
        if (isNull(categoryId)) {
            throw ProductExceptions.EX_CATEGORY_ID_NOT_INFORMED;
        }

        return repository.findByCategoryId(categoryId)
            .stream()
            .map(ProductResponse::of)
            .collect(Collectors.toList());
    }

    public List<ProductResponse> findBySupplierId(Integer supplierId) {
        if (isNull(supplierId)) {
            throw ProductExceptions.EX_SUPPLIER_ID_NOT_INFORMED;
        }

        return repository.findBySupplierId(supplierId)
            .stream()
            .map(ProductResponse::of)
            .collect(Collectors.toList());
    }

    private void validateProductData(ProductRequest request) {
        validateName(request);
        validateQuantity(request);
        validateCategoryAndSupplier(request);
    }

    private void validateName(ProductRequest request) {
        if (isEmpty(request.getName())) {
            throw ProductExceptions.EX_NAME_NOT_INFORMED;
        }
    }

    private void validateQuantity(ProductRequest request) {
        var quantity = request.getQuantityAvailable();

        if (isNull(quantity)) {
            throw ProductExceptions.EX_QUANTITY_NOT_INFORMED;
        }

        if (quantity <= ZERO) {
            throw ProductExceptions.EX_QUANTITY_INVALID;
        }
    }

    private void validateCategoryAndSupplier(ProductRequest request) {
        if (isNull(request.getCategoryId())) {
            throw ProductExceptions.EX_CATEGORY_ID_NOT_INFORMED;
        }

        if (isNull(request.getSupplierId())) {
            throw ProductExceptions.EX_SUPPLIER_ID_NOT_INFORMED;
        }
    }
}
