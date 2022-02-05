package br.com.comunicacaomicrosservicos.productapi.modules.category.utils;

import br.com.comunicacaomicrosservicos.productapi.config.exception.ValidationException;

public class CategoryExceptions {

    public static ValidationException EX_DESCRIPTION_NOT_INFORMED =
        new ValidationException("The category description was not informed.");

    public static ValidationException EX_ID_NOT_INFORMED =
        new ValidationException("The category ID was not informed.");

    public static ValidationException EX_ID_NOT_EXISTS =
        new ValidationException("There's no category for the given ID.");

    public static ValidationException EX_ALREADY_DEFINED_PRODUCT_BY_ID =
        new ValidationException("You cannot delete this category because it's already defined by a product.");
}
