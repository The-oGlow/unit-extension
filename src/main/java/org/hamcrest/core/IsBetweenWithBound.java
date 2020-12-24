package org.hamcrest.core;

import org.hamcrest.Matcher;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.notNullValue;

/**
 * A matcher, which verifies if a value is in a specific range, including the bounderies.
 *
 * @param <T> the type of the class which will be checked
 *
 * @author Oliver Glowa
 * @since 0.01.000
 */
public class IsBetweenWithBound<T extends Comparable<T>> extends IsBetween<T> {

    protected IsBetweenWithBound(final T from, final T to) {
        super(from, to);
    }

    @Override
    protected void verifyInput(final T from, final T to) {
        assertThat(from, notNullValue());
        assertThat(to, notNullValue());
        assertThat(to, greaterThanOrEqualTo(from));
    }

    @Override
    protected void initDescription() {
        DESC_MISMATCH.appendText(" is not between ").appendValue(from).appendText(" and ").appendValue(to).appendText(". Range start and end ARE included.");
        DESC_DESCRIPTION.appendText("The value must be between ").appendValue(from).appendText(" and ").appendValue(to)
                .appendText(". Range start and end ARE included. ");
    }

    public static <T extends Comparable<T>> Matcher<T> between(final T from, final T to) {
        return new IsBetweenWithBound<>(from, to);
    }

    @Override
    protected boolean matchesSafely(final T item) {
        if (item == null) {
            return false;
        } else {
            return (item.compareTo(from) >= 0) && (item.compareTo(to) <= 0);
        }
    }
}
