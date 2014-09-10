package org.validcool;

import org.junit.Test;

import static org.validcool.Validations.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class Checking {

    @Test
    public void check_win() {
        boolean checkTrue = check(null, nullValue());
        assertThat("check should return true", checkTrue);
    }

    @Test
    public void check_fail() {
        boolean checkFalse = !check(null, not(nullValue()));
        assertThat("check should return false", checkFalse);
    }

}
