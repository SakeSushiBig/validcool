# validcool

A plain java library offering a [hamcrest like](https://code.google.com/p/hamcrest/) approach to validation. Here some examples:

```java
validate(name, not(isNullOrEmptyString()).and(matches("[A-Z][a-z]+"));
validate(person, with(Person::getAge, greaterThan(17)));
validate(asList("wolverine", "iceman", "beast"), containsInorder(asList("storm", "xavier", "wolverine", "iceman", "beast", "rouge")));
validate("hello world", endsWith("moon").or(startsWith("hello")));
```
