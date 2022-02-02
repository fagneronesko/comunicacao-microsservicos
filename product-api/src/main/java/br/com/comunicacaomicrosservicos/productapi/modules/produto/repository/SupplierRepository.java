package br.com.comunicacaomicrosservicos.productapi.modules.produto.repository;

import br.com.comunicacaomicrosservicos.productapi.modules.produto.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier, Integer> {
}
