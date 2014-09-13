package org.validcool.validators;

import org.validcool.Validator;

import java.util.function.Function;

public class WithValidator<E, S> extends Validator<E> {

    private Function<E, S> selector;
    private Validator<S> validator;
    private S selection;
    private String propertyName;

    public WithValidator(Function<E, S> selector, Validator<S> validator) {
        super(null, validator.getErrorMessagePattern());
        this.selector = selector;
        this.validator = validator;
    }

    public @Override boolean test(E actual) {
        this.selection = selector.apply(actual);
        return validator.test(selection);
    }

    public WithValidator(String propertyName, Function<E, S> selector, Validator<S> validator) {
        this(selector, validator);
        this.propertyName = propertyName;
    }

    public @Override String createErrorMessage(E actual) {
        String selectorString = propertyName != null ? propertyName : String.valueOf(selection);
        return super.createErrorMessage(String.format("%s.%s", String.valueOf(actual), selectorString));
    }

    public @Override String createErrorMessage(String actualString) {
        if(propertyName != null) {
            actualString += "." + propertyName;
        } else {
            actualString += "." + String.valueOf(selection);
        }
        return super.createErrorMessage(actualString);
    }

}
