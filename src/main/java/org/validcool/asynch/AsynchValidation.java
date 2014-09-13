package org.validcool.asynch;

import org.validcool.Validations;
import org.validcool.Validator;

public class AsynchValidation<E> {

    private ValidationHint hint;
    private String propertyName;
    private E actual;
    private Validator<E> validator;

    public AsynchValidation(String propertyName, E actual, Validator<E> validator, ValidationHint hint) {
        this.propertyName = propertyName;
        this.actual = actual;
        this.validator = validator;
        this.hint = hint;
    }

    public AsynchValidation whenValid(Runnable action) {
        action.run();
        return this;
    }

    public ValidationHint getHint() {
        return hint;
    }

    public boolean validate() {
        boolean isValid = validator.test(actual);
        if(!isValid) {
            String errorMessage;
            if(propertyName != null) {
                errorMessage = validator.createErrorMessage(propertyName);
            } else {
                errorMessage = validator.createErrorMessage(actual);
            }
            Validations.validcoolConfig.handle(errorMessage);
        }
        return isValid;
    }
}
