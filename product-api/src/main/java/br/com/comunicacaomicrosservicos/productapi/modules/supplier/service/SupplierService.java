package br.com.comunicacaomicrosservicos.productapi.modules.supplier.service;

import br.com.comunicacaomicrosservicos.productapi.modules.supplier.dto.SupplierRequest;
import br.com.comunicacaomicrosservicos.productapi.modules.supplier.dto.SupplierResponse;
import br.com.comunicacaomicrosservicos.productapi.modules.supplier.model.Supplier;
import br.com.comunicacaomicrosservicos.productapi.modules.supplier.repository.SupplierRepository;
import br.com.comunicacaomicrosservicos.productapi.modules.supplier.utils.SupplierExceptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static org.springframework.util.ObjectUtils.isEmpty;

@Service
public class SupplierService {

    @Autowired
    private SupplierRepository repository;

    public SupplierResponse save(SupplierRequest request) {
        validateName(request);
        return SupplierResponse.of(repository.save(Supplier.of(request)));
    }

    public Supplier findById(Integer id) {
        if (isNull(id)) {
            throw SupplierExceptions.EX_NAME_NOT_INFORMED;
        }
        return repository.findById(id)
            .orElseThrow(() -> SupplierExceptions.EX_ID_NOT_EXISTS);
    }

    public List<SupplierResponse> findAll() {
        return repository.findAll()
            .stream()
            .map(SupplierResponse::of)
            .collect(Collectors.toList());
    }

    public List<SupplierResponse> findByName(String name) {
        if (isEmpty(name)) {
            throw SupplierExceptions.EX_NAME_NOT_INFORMED;
        }

        return repository.findByNameIgnoreCaseContaining(name)
            .stream()
            .map(SupplierResponse::of)
            .collect(Collectors.toList());
    }

    private void validateName(SupplierRequest request) {
        if (isEmpty(request.getName())) {
            throw SupplierExceptions.EX_NAME_NOT_INFORMED;
        }
    }
}
