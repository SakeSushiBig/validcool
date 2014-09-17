package org.validcool.validators;

import org.validcool.Validator;

import java.util.Collection;

public class AnyValidator<E> extends Validator<E> {

    private Collection<Validator<E>> validators;

    public AnyValidator(Collection<Validator<E>> validators) {
        super(null, null);
        this.validators = validators;
    }

    public @Override boolean test(E actual) {
        return validators.stream().anyMatch(v -> v.test(actual));
    }

    public @Override String createErrorMessage(String actualString) {
        return validators.stream()
                .map(v -> v.createErrorMessage(actualString))
                .reduce("", (a,b) -> a + b + ";").trim();
    }

}
