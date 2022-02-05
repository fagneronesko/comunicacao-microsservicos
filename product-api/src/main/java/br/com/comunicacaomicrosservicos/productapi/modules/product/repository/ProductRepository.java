package br.com.comunicacaomicrosservicos.productapi.modules.product.repository;

import br.com.comunicacaomicrosservicos.productapi.modules.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findByNameIgnoreCaseContaining(String name);

    List<Product> findByCategoryId(Integer categoryId);

    List<Product> findBySupplierId(Integer supplierId);

    Boolean existsByCategoryId(Integer categoryId);

    Boolean existsBySupplierId(Integer supplierId);
}
