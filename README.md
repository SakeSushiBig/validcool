# validcool

A plain java library offering a [hamcrest like](https://code.google.com/p/hamcrest/) approach to validation. Here some examples:

```java
validate(name, not(isNullOrEmptyString()).and(matches("[A-Z][a-z]+"));
validate(person, with(Person::getAge, greaterThan(17)));
validate(asList("wolverine", "iceman", "beast"), containsInorder(asList("storm", "xavier", "wolverine", "iceman", "beast", "rouge")));
validate("hello world", endsWith("moon").or(startsWith("hello")));
validate(12, is("dividable by 2", val -> val % 2 == 0));
```

Here an overview on its features:

* Very readable and nice looking function flow.
* Auto generated, human readable error messages.
* Logical operatos like `and` and `or` to link validations.
* Rich collection of pre-defined validation operations (even specific ones for strings and collections).
* Fully customizable for error handling and logging.
* Targeting properties when validating an instance like: `validate(person, with(Person::getAge, greaterThan(17)).and(Person::getName, not(isNullOrEmptyString())));`

### quickstart
The comfort of validcool comes mainly, like in hamcrest, from static imports. So first of all you need these three imports in each class using validcool.

```java
// base methods like validate, check, not, equalTo, ...
import static org.validcool.Validations.*;
// specifically for spring validation (e.g. regex matching)
import static org.validcool.StringValidations.*;
// specifically for collection validation (e.g. has same order)
import static org.validcool.CollectionValidations.*;
```

You can also configure how the validator handles errors and also how it should log these:

```java
import static org.validcool.Validations.*;

validcoolConfig.startLogging();
validcoolConfig.stopLogging();
validcoolConfig.setErrorLogger((String errorMessage) -> MyLoggerUtility.logValidationError(errorMessage));
validcoolConfig.setErrorHandler((String errorMessage) -> throw new SuperCoolAndCustomValidationException(errorMessage));
```

By default validcool does not enable logging, and if enabled the default logging action is to forward all error messages to System.err.
The defaul error handling action is to throw an org.validcool.ValidationException.

If you don't want an exception thrown when a validation faild and do not need an error message, you can also use the `check` function instead of `validate`. It will return a boolean indicating if the actual value was valid or not:

```java
if(check(person, not(nullValue()))) {
 // do something
}
```

A validator is a unit of validation, like the pre-defined `notNull()` or `equalTo(...)`. If you want to create a custom one follow this pattern:

```java
public static <E extends Person> Validator<E> isOfAge() {
 return new Validator<E>(
  (E person) -> person.getAge() >= 18,
  "person is of age / 18 years or older",
  (E person) -> String.format("\"%s\" is not of age / 18 years or older", person.toString())
 );
}
```

The second parameter (`description`) will be used when the validator is compund with others, for instance through logical operations like in the following example:

```java
validate("Peter", not(nullValue()).and(equalTo("Anne")));
// -> throws exception with error message: "Peter" is not null but "Peter" not equal to "Anne"
```

There is also an `or` operation for each validator.

If you have to perform a custom validation action, but its not likely you will use it again, you can work with the `is` validator instead of defining validators like above:
```java
validate(person, is("an legal male adult", val -> val.getAge() >= 18 && val.getGender() == Gender.MALE));
```

To learn more, browse through the unit tests and use the extensive javadoc.

### goals for the near future

* adding more pre-defined validators
* publishing validcool on maven central
* allowing to specify configuration on a class level (or something similar)
* making it threadsafe (?, not sure if really necassary)
