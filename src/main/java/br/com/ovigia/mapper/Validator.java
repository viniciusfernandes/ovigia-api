package br.com.ovigia.mapper;

import javax.validation.Validation;

public class Validator {
    private static final javax.validation.Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();

    private Validator() {
    }

    public static void validate(Object source) {
        var violations = VALIDATOR.validate(source);
        if (!violations.isEmpty()) {
            throw new IllegalArgumentException("Validation errors: " + violations);
        }
    }
}
