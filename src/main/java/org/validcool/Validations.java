package org.validcool;

import org.validcool.asynch.AsynchValidation;
import org.validcool.asynch.ValidationHint;
import org.validcool.validators.WithValidator;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.validcool.asynch.ValidationHint.SimpleComputing;

public class Validations {

    /**
     * Central point of validation for the validcool library.
     */
    public static final ValidcoolConfiguration validcoolConfig = new ValidcoolConfiguration();

    /**
     * Executes the specified validator with the actual value. Then it will use the configured handling function iff
     * an invalid result occurs. By default this will trigger a ValidationException with an autogenerated error message.
     */
    public static <E> void validate(E actual, Validator<E> validator) {
        if(!validator.test(actual)) {
            validcoolConfig.handle(validator.createErrorMessage(actual));
        }
    }

    public static <E> void validate(String propertyName, E actual, Validator<E> validator) {
        if(!validator.test(actual)) {
            validcoolConfig.handle(validator.createErrorMessage(propertyName));
        }
    }

    public static <E>AsynchValidation validateAsynch(String propertyName, E actual, Validator<E> validator) {
        return new AsynchValidation<>(propertyName, actual, validator, SimpleComputing);
    }

    public static <E>AsynchValidation validateAsynch(String propertyName, E actual,
                                                     Validator<E> validator, ValidationHint hint) {
        return new AsynchValidation<>(propertyName, actual, validator, hint);
    }

    public static <E>AsynchValidation validateAsynch(E actual, Validator<E> validator) {
        return new AsynchValidation<>(null, actual, validator, SimpleComputing);
    }

    public static <E>AsynchValidation validateAsynch(E actual, Validator<E> validator, ValidationHint hint) {
        return new AsynchValidation<>(null, actual, validator, hint);
    }

    public static void setAndValidate(AsynchValidation...validations) {
        List<AsynchValidation> list = Arrays.asList(validations);
        List<CompletableFuture<Boolean>> futures = new ArrayList<>();
        Queue<AsynchValidation> simpleValidation = new LinkedList<>();
        list.forEach(v -> {
            switch(v.getHint()) {
                case IoOperation:
                case HeavyComputing:
                    futures.add(CompletableFuture.supplyAsync(v::validate));
                    break;
                case SimpleComputing:
                    simpleValidation.offer(v);
                    break;
            }
        });
        futures.add(CompletableFuture.supplyAsync(() -> simpleValidation.stream().allMatch(AsynchValidation::validate)));
        try {
            CompletableFuture[] futureArray = new CompletableFuture[futures.size()];
            futureArray = futures.toArray(futureArray);
            CompletableFuture.allOf(futureArray)
                    .join();
        } catch(CompletionException exception) {
            throw (RuntimeException)exception.getCause();
        }
    }

    /**
     * Executes the specified validator with the actual value. It will trigger the configured logging mechanism iff enabled,
     * and return true iff the validation was successful.
     */
    public static <E> boolean check(E actual, Validator<E> validator) {
        boolean isValid = validator.test(actual);
        if(!isValid) {
            validcoolConfig.logIfEnabled(validator.createErrorMessage(actual));
        }
        return isValid;
    }

    /**
     * Executes the specified validator with the actual value <b>asynchronously</b>.
     * It will trigger the configured logging mechanism iff enabled,
     * and return true iff the validation was successful.
     */
    public static <E> CompletableFuture<Boolean> checkAsynch(E actual, Validator<E> validator) {
        return CompletableFuture.supplyAsync(() -> check(actual, validator));
    }

    /**
     * Allows to perform a validator on a property or calculated value from the actual. This is very useful when validating
     * properties of objects like in this example:
     * <code>
     *     validate(person, with(Person::getAge, greaterThan(17)).and(with(Person::getGender, equalTo("m")));
     * </code>
     */
    public static <E, S> Validator<E> with(Function<E, S> selector, Validator<S> validator) {
        return new WithValidator<>(selector, validator);
    }

    public static <E, S> Validator<E> with(String propertyName, Function<E, S> selector, Validator<S> validator) {
        return new WithValidator<>(propertyName, selector, validator);
    }

    /**
     * Fails when actual value is not null.
     */
    public static <E> Validator<E> nullValue() {
        return new Validator<>(
                val -> val == null,
                "${actual} is not null"
        );
    }

    /**
     * Can be used to negate any validator construct. For example:
     * <code>
     *     validate(result, not(nullValue()));
     * </code>
     */
    public static <E> Validator<E> not(Validator<E> validator) {
        return new Validator<>(
                val -> !validator.test(val),
                validator.getErrorMessagePattern().replace("not ", "")
        );
    }

    /**
     * Fails when the actual value is not contained in the specified collection.
     * The collection must not be null.
     */
    public static <E> Validator<E> in(Collection<E> items) {
        return new Validator<>(
                items::contains,
                String.format("${actual} is not in %s", Arrays.toString(items.toArray()))
        );
    }

    public static <E extends Comparable<E>> Validator<E> greaterThan(E other) {
        return new Validator<>(
                val -> val.compareTo(other) > 0,
                String.format("${actual} is not greater than %s", other)
        );
    }

    public static <E extends Comparable<E>> Validator<E> lowerThan(E other) {
        return new Validator<>(
                val -> val.compareTo(other) < 0,
                String.format("${actual} is not lower than %s", other)
        );
    }

    public static <E> Validator<E> equalTo(E other) {
        return new Validator<>(
                val -> val.equals(other),
                String.format("${actual} is not equal to %s", other)
        );
    }

    /**
     * Validates using a predicate function. Usage:
     * <code>
     *     validate(number, is("dividable by 2", val -> val % 2 == 0));
     * </code>
     * @param description tells what is validates (e.g. "contained in array [1,2,3]" or "smaller than 12"),
     *                    the error message will be created in this schema: "$actual" is not $description.
     */
    public static <E> Validator<E> is(String description, Predicate<E> predicate) {
        String verb = "is not";
        if(description.startsWith("not")) {
            verb = "is";
            description = description.replace("not ", "");
        }
        return new Validator<>(
                predicate,
                String.format("${actual} %s %s", verb, description)
        );
    }

}
