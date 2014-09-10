package org.validcool;

import java.util.function.Function;

public class Validator<E> {

    private Function<E, Boolean> validator;
    private String descr;
    private Function<E, String> onError;

    private Boolean valid;
    private String errorMessage;


    public Validator(Function<E, Boolean> validator, String descr, Function<E, String> onError) {
        this.validator = validator;
        this.descr = descr;
        this.onError = onError;
        this.valid = null;
    }

    public void apply(E actual) {
        valid = validator.apply(actual);
        if(!valid) {
            errorMessage = onError.apply(actual);
        }
    }

    public String getDescription() {
        return descr;
    }

    public boolean isValid() {
        return valid;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

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
