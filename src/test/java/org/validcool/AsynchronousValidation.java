package org.validcool;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.concurrent.ExecutionException;

import static java.lang.String.format;
import static org.junit.Assert.assertTrue;
import static org.validcool.Validations.*;

public class AsynchronousValidation {

    @BeforeClass
    public static void setupValidators() {
        validcoolConfig.startLogging();
    }

    @Test
    public void validateAsynch_win() throws ExecutionException, InterruptedException {
        // Arrange
        String actual = "hello world";
        // Act
        String retrievedActual = validateAsynch(actual, not(nullValue())).get();
        // Assert
        assertTrue(format("expected \"%s\" but got \"%s\"", actual, retrievedActual), actual.equals(retrievedActual));
    }

    @Test
    public void validateCheck_win() throws ExecutionException, InterruptedException {
        // Arrange
        int someNum = 12;
        // Act
        boolean isValid = checkAsynch(someNum, greaterThan(10)).get();
        // Assert
        assertTrue(isValid);
    }

    @Test
    public void validateAsynch_fail() throws InterruptedException {
        // Arrange
        double actual = 0.1;
        // Act
        try {
            validateAsynch(actual, greaterThan(1.0)).get();
            assertTrue("no exception raised", false);
        }  catch (ExecutionException exceptionWrapper) {
            Throwable exception = exceptionWrapper.getCause();
            assertTrue(
                    format("expected exception to be not null and instance of ValidationException but got \"%s\"", exception),
                    exception != null && exception instanceof ValidationException);
        }
    }

    @Test
    public void checkAsynch_fail() throws InterruptedException, ExecutionException {
        assertTrue(!checkAsynch("wolverine", nullValue()).get());
    }

}
