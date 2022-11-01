package org.hamcrest;

import org.hamcrest.core.FailWith;

public class FailWithMatcher {

    private FailWithMatcher() {
        //nothing2do
    }

    public static <E extends Throwable> Matcher<E> failWith(Class<E> expectedException) {
        return FailWith.failWith(expectedException);
    }
}
