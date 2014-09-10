package org.validcool;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.validcool.Validations.*;
import static org.validcool.StringValidations.*;

public class ValidatingStrings {

    @BeforeClass
    public static void setupValidators() {
        ValidatorsConfig.printFailOutput = true;
    }

    @Test(expected = ValidationException.class)
    public void failOnMatching() {
        // Arrange
        String pattern = "[a|b]";
        String actual = "c";
        // Act
        validate(actual, matches(pattern));
    }

    @Test
    public void winOnMatching() {
        // Arrange
        String pattern = "[a|b]";
        String actual = "a";
        // Act
        validate(actual, matches(pattern));
    }

    @Test(expected = ValidationException.class)
    public void failOnSubstring() {
        // Arrange
        String phrase = "Hello world!";
        String word = "engage";
        // Act
        validate(word, isSubstringOf(phrase));
    }

    @Test
    public void winOnSubstring() {
        // Arrange
        String phrase = "Lt. LaForge. Engage!";
        String word = "Engage";
        // Act
        validate(word, isSubstringOf(phrase));
    }

    @Test(expected = ValidationException.class)
    public void failOnContains() {
        // Arrange
        String actual = "hello world";
        String expected = "goodbye";
        // Act
        validate(actual, contains(expected));
    }

    @Test
    public void winOnContains() {
        // Arrange
        String actual = "hello world";
        String expected = "world";
        // Act
        validate(actual, contains(expected));
    }

    @Test(expected = ValidationException.class)
    public void failOnHasLength() {
        // Arrange
        String actual = "hello";
        int length = 4;
        // Act
        validate(actual, hasLength(length));
    }

    @Test
    public void winOnHasLength() {
        // Arrange
        String actual = "hello";
        int length = 5;
        // Act
        validate(actual, hasLength(length));
    }

    @Test(expected = ValidationException.class)
    public void failOnEqualIgnoreCase() {
        // Arrange
        String expected = "hEllo";
        String actual = "heLlo1";
        // Act
        validate(actual, equalIgnoreCase(expected));
    }

    @Test
    public void winOnEqualIgnoreCase() {
        // Arrange
        String expected = "hEllo";
        String actual = "hEllo";
        // Act
        validate(actual, equalIgnoreCase(expected));
    }

    @Test
    public void winStartsWith() {
        // Arrange
        String expected = "Captain";
        String actual = "Captain Picard";
        // Act
        validate(actual, startsWith(expected));
    }

    @Test(expected = ValidationException.class)
    public void failStartWith() {
        // Arrange
        String expected = "Mr.";
        String actual = "Lt Commander Data";
        // Act
        validate(actual, startsWith(expected));
    }

    @Test
    public void winEndsWith() {
        // Arrange
        String expected = "Picard";
        String actual = "Captain Picard";
        // Act
        validate(actual, endsWith(expected));
    }

    @Test(expected = ValidationException.class)
    public void failEndsWith() {
        // Arrange
        String expected = "LaForge";
        String actual = "Lt Commander Data";
        // Act
        validate(actual, endsWith(expected));
    }

    @Test
    public void winIsNullOrEmpty() {
        // Act
        validate(null, isNullOrEmptyString());
        validate("", isNullOrEmptyString());
    }

    @Test(expected = ValidationException.class)
    public void failIsNullOrEmpty() {
        // Arrange
        String actual = "hello world";
        // Act
        validate(actual, isNullOrEmptyString());
    }

    @Test
    public void winIsWhitespace() {
        // Arrange
        String oneWhitespace = " ";
        String tabs = "\t\t";
        // Act
        validate(oneWhitespace, isWhitespace());
        validate(tabs, isWhitespace());
    }

    @Test(expected = ValidationException.class)
    public void failIsWhitespace() {
        // Arrange
        String someString = " hello\t";
        // Act
        validate(someString, isWhitespace());
    }

}
