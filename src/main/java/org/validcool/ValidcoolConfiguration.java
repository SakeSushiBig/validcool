package org.validcool;

import java.util.function.Consumer;

/**
 * Central point of configuration for validcool.
 * @author amp
 * @since 1.0
 */
public class ValidcoolConfiguration {

    private boolean logErrorMessages;
    private Consumer<String> errorLogger;
    private Consumer<String> errorHandler;

    ValidcoolConfiguration() {
        logErrorMessages = false;
        errorLogger = System.err::println;
        errorHandler = (String message) -> {
            throw new ValidationException(message);
        };
    }

    /**
     * When a validation fails, it will be logged.
     */
    public void startLogging() {
        this.logErrorMessages = true;
    }

    /**
     * Stops to logIfEnabled error messages when validations fail.
     */
    public void stopLogging() {
        this.logErrorMessages = false;
    }

    /**
     * Whether error messages are logged when validations fail.
     */
    public boolean isLogging() {
        return this.logErrorMessages;
    }

    /**
     * Tells validcool how to logIfEnabled error messages, when validations fail.
     * By default all error messages are redirected to System.err.
     */
    public void setErrorLogger(Consumer<String> errorLogger) {
        this.errorLogger = errorLogger;
    }

    /**
     * Logs the specified error message iff logging is enabled (by calling startLogging).
     */
    void logIfEnabled(String errorMessage) {
        if(logErrorMessages) {
            errorLogger.accept(errorMessage);
        }
    }

    /**
     * Tells validcool how to handle failed validations.
     * By default an org.validcool.ValidationException is thrown.
     */
    public void setErrorHandler(Consumer<String> errorHandler) {
        if(errorHandler == null) {
            throw new IllegalArgumentException("tried to set validcool error handling function to null", new NullPointerException());
        }
        this.errorHandler = errorHandler;
    }

    /**
     * Handles validation error as specified in the configuration class.
     */
    public void handle(String errorMessage) {
        logIfEnabled(errorMessage);
        this.errorHandler.accept(errorMessage);
    }

}
