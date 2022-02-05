package br.com.comunicacaomicrosservicos.productapi.modules.supplier.utils;

import br.com.comunicacaomicrosservicos.productapi.config.exception.ValidationException;

public class SupplierExceptions {

    public static ValidationException EX_NAME_NOT_INFORMED =
        new ValidationException("The supplier name was not informed.");

    public static ValidationException EX_ID_NOT_INFORMED =
        new ValidationException("The supplier ID was not informed.");

    public static ValidationException EX_ID_NOT_EXISTS =
        new ValidationException("There's no supplier for the given ID.");
}
