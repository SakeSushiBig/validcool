package org.validcool;

import java.util.regex.Pattern;

public class StringValidations {

    /**
     * Matches actual string against a regex pattern using @see java.util.regex.Pattern
     */
    public static <E extends CharSequence> Validator<E> matches(String pattern) {
        return new Validator<>(
                (E value) -> Pattern.matches(pattern, value),
                String.format("matches \"%s\"", pattern),
                (E value) -> String.format("\"%s\" does not match pattern \"%s\"", value, pattern)
        );
    }

    /**
     * Fails when actual is no substring of string.
     */
    public static <E extends String> Validator<E> isSubstringOf(String string) {
        return new Validator<>(
                string::contains,
                String.format("substring of \"%s\"", string),
                (E value) -> String.format("\"%s\" is no substring of \"%s\"", value, string)
        );
    }

    /**
     * Fails when actual does not contain other.
     */
    public static <E extends String> Validator<E> contains(String other) {
        return new Validator<>(
                (E value) -> value.contains(other),
                String.format("contains \"%s\"", other),
                (E value) -> String.format("\"%s\" does not contain \"%s\"", value, other)
        );
    }

    public static <E extends CharSequence> Validator<E> hasLength(int length) {
        return new Validator<>(
                (E value) -> value.length() == length,
                String.format("string with length of %d", length),
                (E value) -> String.format("Expected string with length of %d but received %d (\"%s\").", length, value.length(), value)
        );
    }

    public static <E extends String> Validator<E> equalIgnoreCase(String other) {
        return new Validator<>(
                (E value) -> other.compareToIgnoreCase(value) == 0,
                String.format("comparing, ignoring case, to \"%s\"", other),
                (E value) -> String.format("Expected \"%s\" but got \"%s\".", other, value)
        );
    }

    public static <E extends String> Validator<E> startsWith(String start) {
        return new Validator<>(
                (E value) -> value.startsWith(start),
                String.format("starts with \"%s\"", start),
                (E value) -> String.format("\"%s\" doesn't start with \"%s\"", value, start)
        );
    }

    public static <E extends String> Validator<E> endsWith(String end) {
        return new Validator<>(
                (E value) -> value.endsWith(end),
                String.format("ends with \"%s\"", end),
                (E value) -> String.format("\"%s\" doesn't start with \"%s\"", value, end)
        );
    }

    public static <E extends String> Validator<E> isNullOrEmptyString() {
        return new Validator<>(
                (E value) -> value == null || value.isEmpty(),
                String.format("null or empty string"),
                (E value) -> String.format("expected null or empty string but got \"%s\"", value)
        );
    }

    /**
     * Fails when actual string only consists of tabs, linebreaks, whitespaces or no symbols at all.
     */
    public static <E extends String> Validator<E> isWhitespace() {
        return new Validator<>(
                (E value) -> value.trim().isEmpty(),
                "empty string",
                (E value) -> String.format("expected only whitespaces but got \"%s\"", value)
        );
    }
}
