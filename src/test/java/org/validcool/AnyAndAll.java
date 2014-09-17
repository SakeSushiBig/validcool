package org.validcool;

import org.junit.Test;

import java.time.LocalDate;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.validcool.Validations.*;
import static org.validcool.StringValidations.*;

public class AnyAndAll {

    @Test
    public void all_win() {
        LocalDate today = LocalDate.now();
        validate(today, all(not(nullValue()), greaterThan(LocalDate.now().minusYears(18))));
    }

    @Test
    public void all_fail() {
        testErrorMessage(
                () -> validate("something", null, all(not(nullValue()), greaterThan(LocalDate.now()))),
                "something is null");
    }

    @Test
    public void any_win() {
        String someString = "hello";
        validate(someString, any(isNullOrEmptyString(), hasLength(5)));
    }

    @Test(expected = ValidationException.class)
    public void any_fail() {
        String someString = "hell";
        validate(someString, any(isNullOrEmptyString(), hasLength(5)));
    }

    @Test
    public void anyAndAll_win() {
        String someString = "hello";
        validate(someString, any(isNullOrEmptyString(), all(hasLength(5), matches("[a-z]+"))));
    }

    private void testErrorMessage(Runnable validationAction, String expectedErrorMessage) {
        try {
            validationAction.run();
            // fail test when no ValidationException was thrown
            assertThat("should have thrown ValidationException", false);
        } catch(ValidationException e) {
            assertThat(e.getMessage(), equalTo(expectedErrorMessage));
        }
    }

}
