package org.validcool;

import org.junit.Before;
import static org.validcool.Validations.validcoolConfig;

public abstract class ValidationErrorLogging {

    @Before
    public void setupLogging() {
        validcoolConfig.setErrorLogger(System.out::println);
        validcoolConfig.startLogging();
    }

}
