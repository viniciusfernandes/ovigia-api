package br.com.ovigia.mapper;

import br.com.ovigia.error.InvalidRequestException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;

public class Validator {
    private static final javax.validation.Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();

    private Validator() {
    }

    public static void validate(Object source) {
        var violations = VALIDATOR.validate(source);
        if (!violations.isEmpty()) {
            var mensagem = new StringBuilder();
            var invalids = violations.toArray(new ConstraintViolation[]{});
            ConstraintViolation v;
            for (int i = 0; i < invalids.length; i++) {
                v = invalids[i];
                mensagem.append(String.format("O campo \"%s\" %s", v.getPropertyPath(), v.getMessage()));
                if (i < invalids.length - 1) {
                    mensagem.append(",");
                }
            }
            throw new InvalidRequestException(mensagem.toString());
        }
    }
}
