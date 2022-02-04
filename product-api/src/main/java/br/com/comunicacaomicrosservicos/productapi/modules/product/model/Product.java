package br.com.comunicacaomicrosservicos.productapi.modules.product.model;

import br.com.comunicacaomicrosservicos.productapi.modules.category.model.Category;
import br.com.comunicacaomicrosservicos.productapi.modules.product.dto.ProductRequest;
import br.com.comunicacaomicrosservicos.productapi.modules.supplier.model.Supplier;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "PRODUCT")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "QUANTITY_AVAILABLE", nullable = false)
    private Integer quantityAvailable;

    @Column(name = "CREATED_AT", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "FK_CATEGORY", nullable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "FK_SUPPLIER", nullable = false)
    private Supplier supplier;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }

    public static Product of(ProductRequest request, Category category, Supplier supplier) {
        return Product.builder()
            .name(request.getName())
            .quantityAvailable(request.getQuantityAvailable())
            .category(category)
            .supplier(supplier)
            .build();
    }
}
