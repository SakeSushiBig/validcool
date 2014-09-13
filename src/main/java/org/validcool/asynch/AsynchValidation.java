package org.validcool.asynch;

import org.validcool.Validations;
import org.validcool.Validator;

import java.util.concurrent.CompletableFuture;

/**
 * Allows asynchronous execution of validation by passing to @see org.validcool.Validations validate function or
 * by calling the run() method. It is initialized by the validateAsynch functions in @see org.validcool.Validations.
 */
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

    /**
     * Validate synchronously.
     */
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

    /**
     * Validate on JVMs common ForkJoinPool.
     * @return a future on the validation process
     */
    public CompletableFuture<Boolean> run() {
        return CompletableFuture.supplyAsync(this::validate);
    }
}
