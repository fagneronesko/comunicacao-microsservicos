package br.com.comunicacaomicrosservicos.productapi.modules.product.service;

import br.com.comunicacaomicrosservicos.productapi.config.exception.SucessResponse;
import br.com.comunicacaomicrosservicos.productapi.config.exception.ValidationException;
import br.com.comunicacaomicrosservicos.productapi.modules.category.service.CategoryService;
import br.com.comunicacaomicrosservicos.productapi.modules.product.dto.*;
import br.com.comunicacaomicrosservicos.productapi.modules.product.model.Product;
import br.com.comunicacaomicrosservicos.productapi.modules.product.repository.ProductRepository;
import br.com.comunicacaomicrosservicos.productapi.modules.product.utils.ProductExceptions;
import br.com.comunicacaomicrosservicos.productapi.modules.rabbitmq.SalesConfirmationSender;
import br.com.comunicacaomicrosservicos.productapi.modules.sales.client.SalesClient;
import br.com.comunicacaomicrosservicos.productapi.modules.sales.dto.SalesConfirmationDto;
import br.com.comunicacaomicrosservicos.productapi.modules.sales.dto.SalesProductResponse;
import br.com.comunicacaomicrosservicos.productapi.modules.sales.enums.ESalesStatus;
import br.com.comunicacaomicrosservicos.productapi.modules.supplier.service.SupplierService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static br.com.comunicacaomicrosservicos.productapi.config.RequestUtil.getCurrentRequest;
import static br.com.comunicacaomicrosservicos.productapi.modules.product.utils.ProductConstants.*;
import static br.com.comunicacaomicrosservicos.productapi.modules.product.utils.ProductExceptions.*;
import static java.util.Objects.isNull;
import static org.springframework.util.ObjectUtils.isEmpty;

@Slf4j
@Service
public class ProductService {

    private static final Integer ZERO = 0;
    private static final String TRANSACTION_ID = "transactionid";
    private static final String SERVICE_ID = "serviceid";

    @Autowired
    private ProductRepository repository;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private SupplierService supplierService;
    @Autowired
    private SalesConfirmationSender salesConfirmationSender;
    @Autowired
    private SalesClient salesClient;

    public ProductResponse save(ProductRequest request) {
        validateProductData(request);

        return ProductResponse.of(
            repository.save(
                Product.of(
                    request,
                    categoryService.findById(request.getCategoryId()),
                    supplierService.findById(request.getSupplierId()))));
    }

    public ProductResponse update(ProductRequest request, Integer id) {
        validateProductData(request);
        validateId(id);

        var product = Product.of(
            request,
            categoryService.findById(request.getCategoryId()),
            supplierService.findById(request.getSupplierId()));
        product.setId(id);

        return ProductResponse.of(repository.save(product));
    }

    public Product findById(Integer id) {
        validateId(id);
        return repository.findById(id)
            .orElseThrow(() -> EX_ID_NOT_EXISTS);
    }

    public List<ProductResponse> findAll() {
        return repository.findAll()
            .stream()
            .map(ProductResponse::of)
            .collect(Collectors.toList());
    }

    public List<ProductResponse> findByName(String name) {
        if (isEmpty(name)) {
            throw EX_NAME_NOT_INFORMED;
        }

        return repository.findByNameIgnoreCaseContaining(name)
            .stream()
            .map(ProductResponse::of)
            .collect(Collectors.toList());
    }

    public List<ProductResponse> findByCategoryId(Integer categoryId) {
        if (isNull(categoryId)) {
            throw EX_CATEGORY_ID_NOT_INFORMED;
        }

        return repository.findByCategoryId(categoryId)
            .stream()
            .map(ProductResponse::of)
            .collect(Collectors.toList());
    }

    public List<ProductResponse> findBySupplierId(Integer supplierId) {
        if (isNull(supplierId)) {
            throw EX_SUPPLIER_ID_NOT_INFORMED;
        }

        return repository.findBySupplierId(supplierId)
            .stream()
            .map(ProductResponse::of)
            .collect(Collectors.toList());
    }

    public SucessResponse delete(Integer id) {
        validateId(id);
        repository.deleteById(id);
        return SucessResponse.create(DELETE_SUCCESS);
    }

    public Boolean existsByCategoryId(Integer categoryId) {
        return repository.existsByCategoryId(categoryId);
    }

    public Boolean existsBySupplierId(Integer supplierId) {
        return repository.existsBySupplierId(supplierId);
    }

    public void updateProductStock(ProductStockDto product) {
        try {
            validateStockUpdateData(product);
            updateStock(product);
        } catch(Exception ex) {
            log.error("Error while trying to update stock for message with error: {}",
                ex.getMessage(), ex);
            salesConfirmationSender.sendSalesConfirmationMessage(
                new SalesConfirmationDto(product.getSalesId(), ESalesStatus.REJECTED, product.getTransactionid()));
        }
    }

    public ProductSalesResponse findProductSales(Integer id) {
        var product = findById(id);
        var sales = getSalesByProductId(id);
        return ProductSalesResponse.of(product, sales.getSalesIds());
    }

    private SalesProductResponse getSalesByProductId(Integer productId) {
        try {
            var currentRequest = getCurrentRequest();
            var transactionId = currentRequest.getHeader(TRANSACTION_ID);
            var serviceId = currentRequest.getAttribute(SERVICE_ID);

            log.info("Sending GET request to orders by productId with data {} | [transactionId: {} | serviceId: {}]",
                productId, transactionId, serviceId);
            var response = salesClient
                .findSalesByProductId(productId)
                .orElseThrow(() -> EX_SALES_NOT_FOUND_BY_PRODUCT);
            log.info("Recieving response from orders by productId with data {} | [transactionId: {} | serviceId: {}]",
                new ObjectMapper().writeValueAsString(response), transactionId, serviceId);

            return response;
        } catch (Exception ex) {
            throw EX_ERROR_GET_PRODUCTS_SALES;
        }
    }

    public SucessResponse checkProductsStock(ProductCheckStockRequest request) {
        try {
            var currentRequest = getCurrentRequest();
            var transactionId = currentRequest.getHeader(TRANSACTION_ID);
            var serviceId = currentRequest.getAttribute(SERVICE_ID);

            log.info("Request to POST product stock with data {} | [transactionId: {} | serviceId: {}]",
                new ObjectMapper().writeValueAsString(request), transactionId, serviceId);

            if (isEmpty(request) || isEmpty(request.getProducts())) {
                throw EX_REQUEST_DATA_EMPTY;
            }
            request
                .getProducts()
                .forEach(this::validateStock);

            var response = SucessResponse.create(STOCK_OK);
            log.info("Response to POST product stock with data {} | [transactionId: {} | serviceId: {}]",
                new ObjectMapper().writeValueAsString(response), transactionId, serviceId);
            return response;
        } catch (Exception ex) {
            throw new ValidationException(ex.getMessage());
        }
    }

    private void validateStock(ProductQuantityDto productQuantity) {
        if (isEmpty(productQuantity) || isEmpty(productQuantity.getQuantity())) {
            throw EX_QUANTITY_OR_PRODUCT_ID_NULL;
        }
        var product = findById(productQuantity.getProductId());
        if (productQuantity.getQuantity() > product.getQuantityAvailable()) {
            throw new ValidationException(String.format(OUT_STOCK, product.getId()));
        }
    }

    private void updateStock(ProductStockDto product) {
        var productsForUpdate = new ArrayList<Product>();
        product
            .getProducts()
            .forEach(salesProduct -> {
                var existingProduct = findById(salesProduct.getProductId());
                validateQuantityInStock(existingProduct, salesProduct);
                existingProduct.updateStock(salesProduct.getQuantity());
                productsForUpdate.add(existingProduct);
            });
        if (!isEmpty(productsForUpdate)) {
            repository.saveAll(productsForUpdate);
            salesConfirmationSender.sendSalesConfirmationMessage(
                new SalesConfirmationDto(product.getSalesId(), ESalesStatus.APPROVED, product.getTransactionid()));
        }
    }

    private void validateQuantityInStock(Product product, ProductQuantityDto salesProduct) {
        if (salesProduct.getQuantity() > product.getQuantityAvailable()) {
            throw new ValidationException(String.format(OUT_STOCK, product.getId()));
        }
    }

    private void validateStockUpdateData(ProductStockDto product) {
        if (isEmpty(product) || isEmpty(product.getSalesId())) {
            throw EX_PRODUCT_OR_SALES_ID_NULL;
        }
        if (isEmpty(product.getProducts())) {
            throw EX_PRODUCTS_EMPTY;
        }

        product.getProducts()
            .forEach(salesProduct -> {
                if (isEmpty(salesProduct.getQuantity()) || isEmpty(salesProduct.getProductId())) {
                    throw EX_QUANTITY_OR_PRODUCT_ID_NULL;
                }
            });
    }

    private void validateProductData(ProductRequest request) {
        validateName(request);
        validateQuantity(request);
        validateCategoryAndSupplier(request);
    }

    private void validateName(ProductRequest request) {
        if (isEmpty(request.getName())) {
            throw EX_NAME_NOT_INFORMED;
        }
    }

    private void validateId(Integer id) {
        if (isNull(id)) {
            throw EX_ID_NOT_INFORMED;
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
