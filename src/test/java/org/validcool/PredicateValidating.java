package org.validcool;

import org.junit.Test;

import static org.validcool.Validations.*;

public class PredicateValidating extends ValidationErrorLogging {

    @Test
    public void validateWithPredicate_win() {
        validate(12, is("dividable by 2", val -> val % 2 == 0));
    }

    @Test(expected = ValidationException.class)
    public void validateWithPredicate_fail() {
        validate(13, is("dividable by 2", val -> val % 2 == 0));
    }

}
