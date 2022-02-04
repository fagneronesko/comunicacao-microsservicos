package br.com.comunicacaomicrosservicos.productapi.modules.product.dto;

import br.com.comunicacaomicrosservicos.productapi.modules.category.dto.CategoryResponse;
import br.com.comunicacaomicrosservicos.productapi.modules.product.model.Product;
import br.com.comunicacaomicrosservicos.productapi.modules.supplier.dto.SupplierResponse;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {

    private Integer id;
    private String name;
    private Integer quantityAvailable;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime createdAt;
    private CategoryResponse category;
    private SupplierResponse supplier;

    public static ProductResponse of(Product product) {
        return ProductResponse.builder()
            .id(product.getId())
            .name(product.getName())
            .quantityAvailable(product.getQuantityAvailable())
            .createdAt(product.getCreatedAt())
            .category(CategoryResponse.of(product.getCategory()))
            .supplier(SupplierResponse.of(product.getSupplier()))
            .build();
    }
}
