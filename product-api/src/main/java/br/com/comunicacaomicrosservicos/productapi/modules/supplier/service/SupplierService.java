package br.com.comunicacaomicrosservicos.productapi.modules.supplier.service;

import br.com.comunicacaomicrosservicos.productapi.config.exception.SucessResponse;
import br.com.comunicacaomicrosservicos.productapi.modules.category.service.CategoryService;
import br.com.comunicacaomicrosservicos.productapi.modules.product.service.ProductService;
import br.com.comunicacaomicrosservicos.productapi.modules.supplier.dto.SupplierRequest;
import br.com.comunicacaomicrosservicos.productapi.modules.supplier.dto.SupplierResponse;
import br.com.comunicacaomicrosservicos.productapi.modules.supplier.model.Supplier;
import br.com.comunicacaomicrosservicos.productapi.modules.supplier.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static br.com.comunicacaomicrosservicos.productapi.modules.supplier.utils.SupplierConstants.DELETE_SUCCCESS;
import static br.com.comunicacaomicrosservicos.productapi.modules.supplier.utils.SupplierExceptions.*;
import static java.util.Objects.isNull;
import static org.springframework.util.ObjectUtils.isEmpty;

@Service
public class SupplierService {

    @Autowired
    private SupplierRepository repository;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductService productService;

    public SupplierResponse save(SupplierRequest request) {
        validateName(request);
        return SupplierResponse.of(repository.save(Supplier.of(request)));
    }

    public SupplierResponse update(SupplierRequest request, Integer id) {
        validateName(request);
        validateId(id);
        var supplier = Supplier.of(request);
        supplier.setId(id);

        return SupplierResponse.of(repository.save(supplier));
    }

    public Supplier findById(Integer id) {
        validateId(id);
        return repository.findById(id)
            .orElseThrow(() -> EX_ID_NOT_EXISTS);
    }

    public List<SupplierResponse> findAll() {
        return repository.findAll()
            .stream()
            .map(SupplierResponse::of)
            .collect(Collectors.toList());
    }

    public List<SupplierResponse> findByName(String name) {
        if (isEmpty(name)) {
            throw EX_NAME_NOT_INFORMED;
        }

        return repository.findByNameIgnoreCaseContaining(name)
            .stream()
            .map(SupplierResponse::of)
            .collect(Collectors.toList());
    }

    public SucessResponse delete(Integer id) {
        validateId(id);
        if (productService.existsBySupplierId(id)) {
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

    private void validateName(SupplierRequest request) {
        if (isEmpty(request.getName())) {
            throw EX_NAME_NOT_INFORMED;
        }
    }
}
