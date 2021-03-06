package org.validcool;

import org.junit.Test;

import java.awt.*;
import java.time.LocalDate;

import static java.util.Arrays.asList;
import static org.validcool.Validations.*;

public class BasicValidating {

    @Test
    public void greaterThan_win() {
        validate(0, lowerThan(1));
    }

    @Test(expected = ValidationException.class)
    public void greaterThan_fail() {
        validate(1000, lowerThan(2));
    }

    @Test
    public void lowerThan_win() {
        validate(1, greaterThan(0));
    }

    @Test(expected = ValidationException.class)
    public void lowerThan_fail() {
        validate(2, greaterThan(1000));
    }

    @Test
    public void validateWithProperty_win() {
        validate(new Point(1, 2), all(with(Point::getX, greaterThan(0.0)), with(Point::getY, greaterThan(1.0))));
    }

    @Test
    public void equalTo_win()  {
        validate(new Point(1, 1), equalTo(new Point(1, 1)));
    }

    @Test(expected = ValidationException.class)
    public void equalTo_fail() {
        validate(new Point(1, 2), equalTo(new Point(2, 1)));
    }

    @Test
    public void andValidators_win() {
        String wolverine = "wolverine";
        String[] xMen = new String[] { "xavier", wolverine, "cyclops", "storm", "marvel girl"};
        validate(wolverine, all(not(nullValue()), in(asList(xMen))));
    }

    @Test(expected = ValidationException.class)
    public void andValidators_fail() {
        validate("", all(not(nullValue()), equalTo("hello world")));
    }

    @Test
    public void orValidators_win() {
        validate("male", any(equalTo("female"), equalTo("male")));
    }

    @Test(expected = ValidationException.class)
    public void orValidators_fail() {
        validate("groot", any(equalTo("female"), equalTo("male")));
    }

}
