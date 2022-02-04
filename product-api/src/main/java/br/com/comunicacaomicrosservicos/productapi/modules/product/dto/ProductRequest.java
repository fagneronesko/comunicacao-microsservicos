package br.com.comunicacaomicrosservicos.productapi.modules.product.dto;

import lombok.Data;

@Data
public class ProductRequest {

    private String name;
    private Integer quantityAvailable;
    private Integer categoryId;
    private Integer supplierId;
}
