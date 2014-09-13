package org.validcool.validators;

import org.validcool.Validator;

public class AndValidator<E> extends Validator<E> {

    private Validator<E> first;
    private boolean isFirstValid;
    private Validator<E> second;

    public AndValidator(Validator<E> first, Validator<E> second) {
        super(null, null);
        this.first = first;
        this.second = second;
    }

    public @Override boolean test(E actual) {
        return (isFirstValid = first.test(actual)) && second.test(actual);
    }

    public @Override String createErrorMessage(String actualString) {
        return !isFirstValid ? first.createErrorMessage(actualString) : second.createErrorMessage(actualString);
    }
}
