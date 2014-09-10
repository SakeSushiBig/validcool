package org.validcool;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Pattern;

public class Validations {

    public static final class ValidatorsConfig {
        public static boolean printFailOutput = false;
        public static Consumer<String> failOutput = System.err::println;
    }

    public static <E> void validate(E actual, Validator<E> validator) {
        validator.apply(actual);
        if(!validator.isValid()) {
            String errorMessage = validator.getErrorMessage();
            if(ValidatorsConfig.printFailOutput) {
                ValidatorsConfig.failOutput.accept(errorMessage);
            }
            throw new ValidationException(errorMessage);
        }
    }

    public static <E> boolean doCheck(E actual, Validator<E> validator) {
        validator.apply(actual);
        return validator.isValid();
    }

    public static <E, S> Validator<E> with(Function<E, S> selector, Validator<S> validator) {
        return new Validator<>(
                (E value) -> {
                    validator.apply(selector.apply(value));
                    return validator.isValid();
                },
                validator.getDescription(),
                (E value) -> validator.getErrorMessage()
        );
    }

    public static <E> Validator<E> nullValue() {
        return new Validator<>(
                (E value) -> value == null,
                "null value",
                (E value) -> String.format("Expected null value but got: \"%s\".", value)
        );
    }

    public static <E> Validator<E> not(Validator<E> validator) {
        return new Validator<>(
                (E value) -> {
                    validator.apply(value);
                    return !validator.isValid();
                },
                "not",
                (E value) -> String.format("Expected not %s but got: \"%s\".", validator.getDescription(), value)
        );
    }

    public static <E> Validator<E> in(Collection<E> items) {
        return new Validator<>(
                items::contains,
                "in",
                (E value) -> String.format("%s is not in %s", value, Arrays.toString(items.toArray()))
        );
    }

    public static <E extends Comparable<E>> Validator<E> greaterThan(E other) {
        return new Validator<>(
                (E value) -> value.compareTo(other) > 0,
                String.format("greater than \"%s\"", other),
                (E value) -> String.format("\"%s\" is not greater than \"%s\"", value, other)
        );
    }

    public static <E extends Comparable<E>> Validator<E> lowerThan(E other) {
        return new Validator<>(
                (E value) -> value.compareTo(other) < 0,
                String.format("lower than \"%s\"", other),
                (E value) -> String.format("\"%s\" is not lower than \"%s\"", value, other)
        );
    }

    public static <E> Validator<E> equalTo(E other) {
        return new Validator<>(
                (E value) -> value.equals(other),
                String.format("equal to \"%s\"", other),
                (E value) -> String.format("\"%s\" is not equal to \"%s\"", value, other)
        );
    }

}
