# validcool

A plain java library offering a [hamcrest like](https://code.google.com/p/hamcrest/) approach to validation. Here some examples:

```java
validate(name, all(not(isNullOrEmptyString()), matches("[A-Z][a-z]+")));
validate(person, with(Person::getAge, greaterThan(17)));
validate(asList("wolverine", "iceman", "beast"), containsInorder(asList("storm", "xavier", "wolverine", "iceman", "beast", "rouge")));
validate("hello world", any(endsWith("moon"), startsWith("hello")));
validate(12, is("dividable by 2", val -> val % 2 == 0));
```

Here an overview on its features:

* Very readable and nice looking function flow.
* Auto generated, human readable error messages.
* Logical operation validators and/any.
* Rich collection of pre-defined validation operations (even specific ones for strings and collections).
* Fully customizable for error handling and logging.
* Targeting properties when validating an instance like: `validate(person, with(Person::getAge, all(greaterThan(17)), Person::getName, not(isNullOrEmptyString())));`

### using with maven or gradle
Validcool is available on sonatypes maven central.

```groovy
repositories {
 mavenCentral()
}

dependencies {
 compile 'org.github.sakesushibig:validcool:0.2'
}
```

```xml
<dependency>
  <groupId>com.github.sakesushibig</groupId>
  <artifactId>validcool</artifactId>
  <version>0.2</version>
</dependency>
```

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
validate("Peter", all(not(nullValue()), equalTo("Anne")));
// -> throws exception with error message: "Peter" is not null but "Peter" not equal to "Anne"
```

There is also an `or` operation for each validator.

If you have to perform a custom validation action, but its not likely you will use it again, you can work with the `is` validator instead of defining validators like above:
```java
validate(person, is("an legal male adult", val -> val.getAge() >= 18 && val.getGender() == Gender.MALE));
```

To learn more, browse through the unit tests and use the extensive javadoc.

### asynchronous validations
When creating or updating big entity objects, with ten upwards properties, it is very likely to encounter a long running validation process. When we think of properties like usernames or facebook IDs the validation even requires IO operations (check if unique or exists). Therefore validcool porivdes us with the asynchronous execution of validators and a simple synchronization mechanism. The following example shows how to design and validate an entity class with validcools asynch package:

```java

import static org.validcool.asynch.ValidationHint.IoOperation;
import static org.validcool.Validations.*;
import static org.validcool.StringValidations.*;

public class Company {
 private String name;
 private String uid;
 private LocalDate foundingDate;
 private Double avgRevenue;

 @Autowired
 private CompanyRepository repository;
 @Autowired
 private EuUidRegistryService uidService;

 public Company(String name, String uid, LocalDate foundingDate, Double avgRevenue) {
  // asynchronously executes and synchronizes the validations
  // will throw a ValidationException when at least one property
  // is not valid
  validate(
   setName(name), setUid(uid),
   setFoundingDate(foundingDate),
   setAvgRevenue(avgRevenue)
  );
 }

 /* the setter methods should follow a certain pattern to optimally
  * use validcools asynchronous validation, as you can see:
  */
 private AsynchValidation setName(String newName) {
  return validateAsynch("name", newName, all(
     not(isNullOrEmptyString()),
     is("not already taken", repository::isNameUnique)), IoOperation)
   .whenValid(() -> name = newName);
 }

 private AsynchValidation setUid(String newUid) {
  return validateAsynch("uid", newUid, any(
     nullValue(),
     is("available in EU registry", uidService::checkAvailability)),
   IoOperation).whenValid(() -> uid = newUid);
 }

 private AsynchValidation setFoundingDate(LocalDate newFoundingDate) {
  return validateAsynch("founding date", newFoundingDate, all(not(nullValue), lowerThan(LocalDate.now())))
   .whenValid(() -> foundingDate = newFoundingDate);
 }

 private AsynchValidation setAvgRevenue(Double newAvgRevenue) {
  return validateAsynch("average revenue", newAvgRevenue, any(nullValue(), greaterThan(0.0)))
   .whenValid(() -> avgRevenue = newAvgRevenue);
 }

 // other getter and domain methods ...

}
```
The so called validation hints, as used when validating name and uid in form of `IoOperation`, tell validcool wheter to execute the validation in a seperate task, or execute it together with other. The free validation hints currently available are:

* `SimpleComputation` default, all validators marked with this are collected into one execution unit.
* `HeavyComputation` executed in its own execution unit.
* `IoOperation` executed in its own execution unit.

Beside the pattern for entity validation, you can also use it in a much simple form:

```java
CompletableFuture future = validateAsynch(someObject, doSomeValidation()).run();
try {
 future.join();
} catch(CompletionException e) {
 throw (ValidationException)e;
}
```

### goals for the near future

* publishing validcool on maven central
* allowing to specify configuration on a class level (or something similar)
* file i/o validators
* reworking the async interface
