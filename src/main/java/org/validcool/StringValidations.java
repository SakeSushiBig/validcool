package org.validcool;

import java.util.regex.Pattern;

public class StringValidations {

    /**
     * Matches actual string against a regex pattern using @see java.util.regex.Pattern
     */
    public static <E extends CharSequence> Validator<E> matches(String pattern) {
        return new Validator<>(
                (E value) -> Pattern.matches(pattern, value),
                String.format("${actual} does not match \"%s\"", pattern)
        );
    }

    /**
     * Fails when actual is no substring of string.
     */
    public static <E extends String> Validator<E> isSubstringOf(String string) {
        return new Validator<>(
                string::contains,
                String.format("${actual} is no substring of \"%s\"", string)
        );
    }

    /**
     * Fails when actual does not contain other.
     */
    public static <E extends String> Validator<E> contains(String other) {
        return new Validator<>(
                (E value) -> value.contains(other),
                String.format("${actual} does not contain \"%s\"", other)
        );
    }

    public static <E extends String> Validator<E> hasLength(int length) {
        return new Validator<>(
                val -> val.length() == length,
                String.format("${actual} does not have length of %d", length)
        );
    }

    public static <E extends String> Validator<E> equalIgnoreCase(String other) {
        return new Validator<>(
                (E value) -> other.compareToIgnoreCase(value) == 0,
                String.format("${actual} does not equal to, ignoring case,  \"%s\"", other)
        );
    }

    public static <E extends String> Validator<E> startsWith(String start) {
        return new Validator<>(
                (E value) -> value.startsWith(start),
                String.format("${actual} does not start with \"%s\"", start)
        );
    }

    public static <E extends String> Validator<E> endsWith(String end) {
        return new Validator<>(
                (E value) -> value.endsWith(end),
                String.format("${actual} does not end with \"%s\"", end)
        );
    }

    public static <E extends String> Validator<E> isNullOrEmptyString() {
        return new Validator<>(
                val -> val == null || val.isEmpty(),
                "${actual} is not null or empty string"
        );
    }

    /**
     * Fails when actual string only consists of tabs, linebreaks, whitespaces or no symbols at all.
     */
    public static <E extends String> Validator<E> isWhitespace() {
        return new Validator<>(
                (E value) -> value.trim().isEmpty(),
                "${actual} is not empty string"
        );
    }
}
