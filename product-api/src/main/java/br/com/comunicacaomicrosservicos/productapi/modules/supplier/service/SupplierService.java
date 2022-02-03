package br.com.comunicacaomicrosservicos.productapi.modules.supplier.service;

import br.com.comunicacaomicrosservicos.productapi.config.exception.ValidationException;
import br.com.comunicacaomicrosservicos.productapi.modules.supplier.dto.SupplierRequest;
import br.com.comunicacaomicrosservicos.productapi.modules.supplier.dto.SupplierResponse;
import br.com.comunicacaomicrosservicos.productapi.modules.supplier.model.Supplier;
import br.com.comunicacaomicrosservicos.productapi.modules.supplier.repository.SupplierRepository;
import br.com.comunicacaomicrosservicos.productapi.modules.supplier.utils.SupplierConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
public class SupplierService {

    @Autowired
    private SupplierRepository repository;

    public SupplierResponse save(SupplierRequest request) {
        var supplier = Supplier.of(request);
        validateName(supplier);

        return SupplierResponse.of(repository.save(supplier));
    }

    private void validateName(Supplier supplier) {
        if (isEmpty(supplier.getName())) {
            throw new ValidationException(SupplierConstants.NAME_NOT_INFORMED);
        }
    }
}
