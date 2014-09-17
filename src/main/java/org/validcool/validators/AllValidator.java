package org.validcool.validators;

import org.validcool.Validator;

import java.util.Collection;
import java.util.Optional;

public class AllValidator<E> extends Validator<E> {

    private Collection<Validator<E>> validators;
    private Optional<Validator<E>> fail;

    public AllValidator(Collection<Validator<E>> validators) {
        super(null, null);
        this.validators = validators;
    }

    public @Override boolean test(E actual) {
        fail = validators.stream().filter(v -> !v.test(actual)).findFirst();
        return !fail.isPresent();
    }

    public @Override String createErrorMessage(String actualString) {
        if(!fail.isPresent()) {
            throw new UnsupportedOperationException("no error message present");
        }
        return fail.get().createErrorMessage(actualString);
    }

}
