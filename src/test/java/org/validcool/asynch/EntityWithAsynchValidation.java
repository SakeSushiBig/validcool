package org.validcool.asynch;

import java.time.LocalDate;
import static org.validcool.Validations.*;
import static org.validcool.StringValidations.*;

public class EntityWithAsynchValidation {

    private String someString;
    private int someInteger;
    private LocalDate someDate;
    private String anotherString;

    public EntityWithAsynchValidation(String someString, int someInteger, LocalDate someDate, String anotherString) {
        validate(
                setSomeString(someString),
                setSomeinteger(someInteger),
                setSomeDate(someDate),
                setAnotherString(anotherString)
        );
    }

    public AsynchValidation setSomeString(String someString) {
        return validateAsynch("some string", someString, not(isNullOrEmptyString()))
                .whenValid(() -> this.someString = someString);
    }

    public AsynchValidation setSomeinteger(int someInteger) {
        return validateAsynch("some integer", someInteger, greaterThan(12))
                .whenValid(() -> this.someInteger = someInteger);
    }

    public AsynchValidation setSomeDate(LocalDate someDate) {
        return validateAsynch("some date", someDate, lowerThan(LocalDate.now().minusYears(18)))
                .whenValid(() -> this.someDate = someDate);
    }

    public AsynchValidation setAnotherString(String anotherString) {
        return validateAsynch("another string", anotherString, is("some long io validation", val -> {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return true;
        }), ValidationHint.IoOperation).whenValid(() -> this.anotherString = anotherString);
    }

}
