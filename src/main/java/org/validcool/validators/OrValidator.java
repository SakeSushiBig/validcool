package org.validcool.validators;

import org.validcool.Validator;

public class OrValidator<E> extends Validator<E> {

    private Validator<E> first;
    private Validator<E> second;
    private boolean isFirstValid;
    private boolean isSecondValid;

    public OrValidator(Validator<E> first, Validator<E> second) {
        super(null, null);
        this.first = first;
        this.second = second;
    }

    public @Override boolean test(E actual) {
        return (isFirstValid = first.test(actual)) || (isSecondValid = second.test(actual));
    }

    public @Override String createErrorMessage(String actualString) {
        StringBuilder sb = new StringBuilder();
        if(!isFirstValid) sb.append(first.createErrorMessage(actualString));
        if(!isFirstValid && !isSecondValid) sb.append(", ");
        if(!isSecondValid) sb.append(second.createErrorMessage(actualString));
        return sb.toString();
    }

}
