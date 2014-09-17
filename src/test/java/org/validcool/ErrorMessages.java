package org.validcool;

import org.junit.Test;

import java.awt.*;

import static org.validcool.StringValidations.isNullOrEmptyString;
import static org.validcool.StringValidations.hasLength;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.validcool.Validations.*;

public class ErrorMessages extends ValidationErrorLogging {

    private void testErrorMessage(Runnable validationAction, String expectedErrorMessage) {
        try {
            validationAction.run();
            // fail test when no ValidationException was thrown
            assertThat("should have thrown ValidationException", false);
        } catch(ValidationException e) {
            assertThat(e.getMessage(), equalTo(expectedErrorMessage));
        }
    }

    @Test
    public void messageOnNullValue() {
        testErrorMessage(() -> validate("hello world", nullValue()), "hello world is not null");
        testErrorMessage(() -> validate("name", null, not(nullValue())), "name is null");
    }

    @Test
    public void messageOnAssociation() {
        testErrorMessage(() -> validate("symbol", "", all(not(isNullOrEmptyString()), (hasLength(1)))),
                "symbol is null or empty string");
        testErrorMessage(() -> validate("symbol", "ab", all(not(isNullOrEmptyString()), hasLength(1))),
                "symbol does not have length of 1");
        testErrorMessage(() -> validate(4, any(greaterThan(5), lowerThan(3))),
                "4 is not greater than 5;4 is not lower than 3;");
    }

    @Test
    public void messageWithProperty() {
        Point point = new Point(1, 3);
        testErrorMessage(() -> validate(point, with(Point::getY, greaterThan(4.0))),
                "java.awt.Point[x=1,y=3].3.0 is not greater than 4.0");
        testErrorMessage(() -> validate("point", point, with("y", Point::getY, greaterThan(4.0))),
                "point.y is not greater than 4.0");
        testErrorMessage(() -> validate(point, with("y", Point::getY, greaterThan(4.0))),
                "java.awt.Point[x=1,y=3].y is not greater than 4.0");
        testErrorMessage(() -> validate("point", point, with(Point::getY, greaterThan(4.0))),
                "point.3.0 is not greater than 4.0");
    }

}
