package org.validcool.validators;

import java.io.IOException;

public interface IoPredicate<E> {
    boolean test(E value) throws IOException;
}
