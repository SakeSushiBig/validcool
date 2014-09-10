package org.validcool;

import org.junit.BeforeClass;
import org.junit.Test;

import static java.util.Arrays.asList;
import static org.validcool.Validations.*;
import static org.validcool.CollectionValidations.*;

public class ValidatingCollections {

    // some test strings
    private String gandalf = "the grey";
    private String bilbo = "baggings";
    private String smaugs = "desolation";
    private String smeagol = "golum";

    @BeforeClass
    public static void setupValidators() {
        validcoolConfig.startLogging();
    }

    @Test
    public void testItemsWin() {
        validate(asList("a", "b", "c", "d"), hasItems(asList("a", "d", "c")));
        validate(asList(), hasItems(asList()));
    }

    @Test(expected = ValidationException.class)
    public void hasItemsFail() {
        validate(asList("a", "b", "c"), hasItems(asList("x", "y", "z")));
    }

    @Test(expected = ValidationException.class)
    public void hasNotAllItemsFail() {
        validate(asList("a", "b", "c"), hasItems(asList("a", "b", "Smaug")));
    }

    @Test
    public void hasAnyWin() {
        validate(asList(gandalf, smeagol, smaugs), hasAny(asList(bilbo, smaugs, smeagol)));
    }

    @Test(expected = ValidationException.class)
    public void hasAnyFail() {
        validate(asList(gandalf, bilbo), hasAny(asList(smeagol, smaugs)));
    }

    @Test
    public void hasItemsInOrder_win() {
        validate(asList(gandalf, bilbo), hasItemsInOrder(asList(gandalf, bilbo)));
        validate(asList(smaugs, null, smeagol), hasItemsInOrder(asList(smaugs, null, smeagol)));
        validate(asList(gandalf, bilbo, smaugs, smeagol), hasItemsInOrder(asList(bilbo, smaugs, smeagol)));
        validate(asList(gandalf, bilbo, smaugs), hasItemsInOrder(asList(gandalf, bilbo)));
        validate(asList(bilbo, smeagol, bilbo, smaugs), hasItemsInOrder(asList(bilbo, smaugs)));
        validate(asList(bilbo), hasItemsInOrder(asList(bilbo)));
    }

    @Test(expected = ValidationException.class)
    public void hasItemsInOrder_reversed_fail() {
        validate(asList(gandalf, bilbo), hasItemsInOrder(asList(bilbo, gandalf)));
    }

    @Test
    public void hasNot_win() {
        validate(asList(gandalf, bilbo), hasNot(asList(smaugs, smeagol)));
        validate(asList(), hasNot(asList(gandalf, bilbo, smaugs, smeagol)));
    }

    @Test(expected = ValidationException.class)
    public void hasNot_butOne_fail() {
        validate(asList(gandalf, bilbo), hasNot(asList(bilbo, smaugs)));
    }

    @Test(expected = ValidationException.class)
    public void hasNot_butTheLast_fail() {
        validate(asList(gandalf, bilbo), hasNot(asList(smaugs, bilbo)));
    }

    @Test
    public void hasNot_none_win() {
        validate(asList(gandalf, bilbo), hasNot(asList()));
    }

    @Test
    public void hasSameItems_win() {
        validate(asList(gandalf, bilbo), sameItems(asList(bilbo, gandalf)));
        validate(asList(gandalf, bilbo), sameItems(asList(gandalf, bilbo)));
    }

    @Test(expected = ValidationException.class)
    public void hasSameItems_fail() {
        validate(asList(gandalf, bilbo), sameItems(asList(gandalf, smeagol)));
    }

    @Test(expected = ValidationException.class)
    public void hasSameItems_butOne_fail() {
        validate(asList(gandalf, bilbo), sameItems(asList(gandalf, bilbo, smaugs)));
    }

    @Test
    public void hasSameItemsInOrder_win() {
        validate(asList(gandalf, bilbo, smaugs, smeagol), sameItemsInOrder(asList(gandalf, bilbo, smaugs, smeagol)));
        validate(asList(), sameItems(asList()));
    }

    @Test(expected = ValidationException.class)
    public void hasSameItemsInOrder_fail() {
        validate(asList(gandalf, bilbo), sameItemsInOrder(asList(bilbo, gandalf)));
    }

    @Test(expected = ValidationException.class)
    public void hasSameItemsInOrder_differentLength_fail() {
        validate(asList(gandalf, bilbo), sameItemsInOrder(asList(bilbo, gandalf, smaugs)));
    }

    @Test
    public void hasSize_win() {
        validate(asList(gandalf, bilbo), hasSize(2));
        validate(asList(),isEmptyCollection());
    }

    @Test(expected = ValidationException.class)
    public void hasSize_smaller_fail() {
        validate(asList(gandalf, bilbo), hasSize(1));
    }

    @Test(expected = ValidationException.class)
    public void hasSize_greater_fail() {
        validate(asList(gandalf, bilbo), hasSize(3));
    }

    @Test(expected = ValidationException.class)
    public void hasSize_notEmpty_fail() {
        validate(asList(gandalf, bilbo), isEmptyCollection());
    }


}
