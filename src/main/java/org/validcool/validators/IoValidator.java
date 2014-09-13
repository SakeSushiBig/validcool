package org.validcool.validators;

import org.validcool.Validator;

import java.io.IOException;
import java.nio.file.Path;

public class IoValidator<E extends Path> extends Validator<E> {

    public IoValidator(IoPredicate<Path> predicate, String errorMessageTemplate) {
        super(val -> {
            try {
                return predicate.test(val);
            } catch(IOException e) {
                throw new RuntimeException(e);
            }
        }, errorMessageTemplate);
    }

}
