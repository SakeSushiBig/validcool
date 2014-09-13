package org.validcool.asynch;

import org.junit.Test;
import org.validcool.ValidationErrorLogging;
import org.validcool.ValidationException;

import java.time.LocalDate;
import java.util.concurrent.ExecutionException;

import static java.lang.String.format;
import static org.junit.Assert.assertTrue;
import static org.validcool.Validations.*;

public class AsynchronousValidation extends ValidationErrorLogging {

    @Test(expected = ValidationException.class)
    public void validateEntityAsynchronously() {
        new EntityWithAsynchValidation("hello", 15, LocalDate.now().minusYears(20), "hello world");
        new EntityWithAsynchValidation(null, 15, LocalDate.now().minusYears(20), "hello world");
    }

}
