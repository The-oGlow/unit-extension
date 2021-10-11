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

    protected static final String RANGE_INFO_INCLUDED = " Range start and end ARE included. ";

    protected IsBetweenWithBound(Range<T> fromTo) {
        super(fromTo);
    }

    @Override
    protected void verifyInput(Range<T> fromTo) {
        assertThat(fromTo, notNullValue());
        assertThat(fromTo.getLeft(), notNullValue());
        assertThat(fromTo.getRight(), notNullValue());
        assertThat(fromTo.getRight(), greaterThanOrEqualTo(fromTo.getLeft()));
    }

    @Override
    protected String getRangeInfo() {
        return RANGE_INFO_INCLUDED;
    }

    @Override
    protected void initDescription() {
        DESC_DESCRIPTION.appendText(String.format(DESC_MISMATCH_TEXT_1, fromTo.getLeft(), fromTo.getRight())).appendText(getRangeInfo());
        DESC_MISMATCH.appendText(String.format(DESC_MISMATCH_TEXT_2, fromTo.getLeft(), fromTo.getRight())).appendText(getRangeInfo());

    }

    public static <T extends Comparable<T>> Matcher<T> between(T from, T to) {
        return between(new Range<>(from, to));
    }

    public static <T extends Comparable<T>> Matcher<T> between(Range<T> fromTo) {
        return new IsBetweenWithBound<>(fromTo);
    }

    @Override
    protected boolean matchesSafely(T item) {
        if (item == null) {
            return false;
        } else {
            return (item.compareTo(fromTo.getLeft()) >= 0) && (item.compareTo(fromTo.getRight()) <= 0);
        }
    }
}
