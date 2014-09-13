package org.validcool.asynch;

import org.junit.Test;
import org.validcool.ValidationErrorLogging;
import org.validcool.ValidationException;

import java.time.LocalDate;
import java.util.concurrent.ExecutionException;

import static org.validcool.Validations.not;
import static org.validcool.Validations.nullValue;
import static org.validcool.Validations.validateAsynch;

public class AsynchronousValidation extends ValidationErrorLogging {

    @Test(expected = ValidationException.class)
    public void validateEntityAsynchronously() {
        new EntityWithAsynchValidation("hello", 15, LocalDate.now().minusYears(20), "hello world");
        new EntityWithAsynchValidation(null, 15, LocalDate.now().minusYears(20), "hello world");
    }

    @Test(expected = ValidationException.class)
    public void validateSingleValidationAsynchronously() throws InterruptedException {
        try {
            validateAsynch(null, not(nullValue())).run().get();
        }  catch (ExecutionException e) {
            throw (RuntimeException)e.getCause();
        }
    }

}
