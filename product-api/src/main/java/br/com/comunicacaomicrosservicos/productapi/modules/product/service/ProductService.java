package br.com.comunicacaomicrosservicos.productapi.modules.product.service;

import br.com.comunicacaomicrosservicos.productapi.modules.category.service.CategoryService;
import br.com.comunicacaomicrosservicos.productapi.modules.product.dto.ProductRequest;
import br.com.comunicacaomicrosservicos.productapi.modules.product.dto.ProductResponse;
import br.com.comunicacaomicrosservicos.productapi.modules.product.model.Product;
import br.com.comunicacaomicrosservicos.productapi.modules.product.repository.ProductRepository;
import br.com.comunicacaomicrosservicos.productapi.modules.product.utils.ProductExceptions;
import br.com.comunicacaomicrosservicos.productapi.modules.supplier.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
