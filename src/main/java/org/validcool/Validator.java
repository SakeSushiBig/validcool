package org.validcool;


import java.util.function.Predicate;

/**
 * Unit of validation. One validator performs one act of validation on a supplied value.
 * This act of validation can also consist of other validators.
 * @param <E> type to validate
 */
public class Validator<E> {

    private Predicate<E> validator;
    private String errorMessagePattern;

    /**
     * @param validator performs the validation
     * @param errorMessagePattern output when error occurs, when contains "${actual}" this will be replaced with the
     *                            actual value
     */
    public Validator(Predicate<E> validator, String errorMessagePattern) {
        this.validator = validator;
        this.errorMessagePattern = errorMessagePattern;
    }

    public boolean test(E actual) {
        return validator.test(actual);
    }

    public String createErrorMessage(E actual) {
        return createErrorMessage(String.valueOf(actual));
    }

    public String createErrorMessage(String actualString) {
        return errorMessagePattern.replace("${actual}", actualString);
    }

    public String getErrorMessagePattern() {
        return errorMessagePattern;
    }

}
