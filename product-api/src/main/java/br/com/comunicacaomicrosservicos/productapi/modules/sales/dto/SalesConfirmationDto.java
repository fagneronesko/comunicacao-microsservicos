package br.com.comunicacaomicrosservicos.productapi.modules.sales.dto;

import br.com.comunicacaomicrosservicos.productapi.modules.sales.enums.ESalesStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SalesConfirmationDto {

    private String salesId;
    private ESalesStatus status;
}
