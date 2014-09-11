package org.validcool;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Unit of validation. One validator performs one act of validation on a supplied value.
 * This act of validation can also consist of other validators.
 * It has state -> when a value is supplied the validator stores if its valid and when not the error message.
 * So validators are not threadsafe and shouldn't be reused.
 * @param <E> type to validate
 */
public class Validator<E> {

    private Predicate<E> validator;
    private String descr;
    private Function<E, String> onError;

    private Boolean valid;
    private String errorMessage;

    /**
     * @param validator performs the validation
     * @param descr describing the validation (e.g.: equal to "Hello World!")
     * @param onError generates error message including value (e.g.: "Anne" not equal to "Peter", "3" not lower than "2")
     */
    public Validator(Predicate<E> validator, String descr, Function<E, String> onError) {
        this.validator = validator;
        this.descr = descr;
        this.onError = onError;
        this.valid = null;
    }

    /**
     * Invoke validation on actual value.
     */
    public void apply(E actual) {
        valid = validator.test(actual);
        if(!valid) {
            errorMessage = onError.apply(actual);
        }
    }

    public String getDescription() {
        return descr;
    }

    /**
     * Whether the validation succeeded or not.
     */
    public boolean isValid() {
        return valid;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Links the validator with the other via the logical and operation. At point of validation both, the validator and
     * the other, need to be valid so the overall validation result will be valid.
     * @return a new Validator requiring current and other to be valid
     */
    public Validator<E> and(Validator<E> other) {
        return new Validator<>(
                (E value) -> {
                    this.apply(value);
                    if(!this.isValid()) {
                        return false;
                    } else {
                        other.apply(value);
                        if(!other.isValid()) {
                            return false;
                        }
                    }
                    return true;
                },
                this.getDescription() + " and " + other.getDescription(),
                (E value) -> !this.isValid() ? this.getErrorMessage() : other.getErrorMessage()
        );
    }

    /**
     * Links the validator with the other via the logical and operation. At point of validation at least one, the validator or
     * the other, need to be valid so the overall validation result will be valid.
     * @return a new Validator requiring at least one of current or other to be valid
     */
    public Validator<E> or(Validator<E> other) {
        return new Validator<>(
                (E value) -> {
                    this.apply(value);
                    if(this.isValid()) {
                        return true;
                    } else {
                        other.apply(value);
                        return other.isValid();
                    }
                },
                String.format("\"%s\" or \"%s\"", this.getDescription(), other.getDescription()),
                (E value) -> String.format("\"%s\" NOR \"%s\"", this.getErrorMessage(), other.getErrorMessage())
        );
    }

}
