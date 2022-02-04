package br.com.comunicacaomicrosservicos.productapi.modules.category.utils;

import br.com.comunicacaomicrosservicos.productapi.config.exception.ValidationException;

public class CategoryExceptions {

    public static ValidationException EX_DESCRIPTION_NOT_INFORMED =
        new ValidationException("The category description was not informed.");

    public static ValidationException EX_ID_NOT_INFORMED =
        new ValidationException("The category ID was not informed.");

    public static ValidationException EX_ID_NOT_EXISTS =
        new ValidationException("There's no category for the given ID.");
}
