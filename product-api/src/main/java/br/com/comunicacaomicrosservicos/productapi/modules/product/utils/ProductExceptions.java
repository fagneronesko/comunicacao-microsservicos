package br.com.comunicacaomicrosservicos.productapi.modules.product.utils;

import br.com.comunicacaomicrosservicos.productapi.config.exception.ValidationException;

public class ProductExceptions {

    public static ValidationException EX_NAME_NOT_INFORMED =
        new ValidationException("The product name was not informed.");

    public static ValidationException EX_ID_NOT_INFORMED =
        new ValidationException("The product ID was not informed.");

    public static ValidationException EX_ID_NOT_EXISTS =
        new ValidationException("There's no product for the given ID.");

    public static ValidationException EX_QUANTITY_NOT_INFORMED =
        new ValidationException("The product quantity was not informed.");

    public static ValidationException EX_CATEGORY_ID_NOT_INFORMED =
        new ValidationException("The category ID was not informed.");

    public static ValidationException EX_SUPPLIER_ID_NOT_INFORMED =
        new ValidationException("The supplier ID was not informed.");

    public static ValidationException EX_QUANTITY_INVALID =
        new ValidationException("The product quantity not be less or equal to zero.");
}
