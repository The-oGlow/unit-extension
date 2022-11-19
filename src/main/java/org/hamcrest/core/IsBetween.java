package org.hamcrest.core;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.hamcrest.TypeSafeMatcher;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;

/**
 * A matcher, which verifies if a value is in a specific range.
 *
 * @param <T> the type of the clazz which will be checked
 *
 * @since 0.1.0
 */
public class IsBetween<T extends Comparable<T>> extends TypeSafeMatcher<T> {

    public static class Range<T> extends MutablePair<T, T> {
        private static final long serialVersionUID = 1; //NOSONAR java:S4926

        /**
         * Create a new range instance.
         *
         * @param from start value of the range
         * @param to   end value of the range
         */
        public Range(T from, T to) {
            super(from, to);
        }
    }

    protected static final String DESC_MISMATCH_TEXT_1 = "The value must be between %s and %s. ";
    protected static final String DESC_MISMATCH_TEXT_2 = " is not between %s and $%s. ";
    protected static final String RANGE_INFO_NOT_INCL  = "Range start and end not included. ";

    protected static final Description DESC_MISMATCH    = new StringDescription();
    protected static final Description DESC_DESCRIPTION = new StringDescription();

    protected Pair<T, T> fromTo;

    @SuppressWarnings("java:S1699")
    protected IsBetween(Range<T> fromTo) {
        super();
        verifyInput(fromTo);
        this.fromTo = fromTo;
        initDescription();
    }

    protected void verifyInput(Range<T> fromTo) {
        assertThat(fromTo, notNullValue());
        assertThat(fromTo.getLeft(), notNullValue());
        assertThat(fromTo.getRight(), notNullValue());
        assertThat(fromTo.getRight(), greaterThan(fromTo.getLeft()));
    }

    protected String getRangeInfo() {
        return RANGE_INFO_NOT_INCL;
    }

    protected void initDescription() {
        DESC_DESCRIPTION.appendText(String.format(DESC_MISMATCH_TEXT_1, fromTo.getLeft(), fromTo.getRight())).appendText(getRangeInfo());
        DESC_MISMATCH.appendText(String.format(DESC_MISMATCH_TEXT_2, fromTo.getLeft(), fromTo.getRight())).appendText(getRangeInfo());
    }

    public static <T extends Comparable<T>> Matcher<T> between(T from, T to) {
        return between(new Range<>(from, to));
    }

    public static <T extends Comparable<T>> Matcher<T> between(Range<T> fromTo) {
        return new IsBetween<>(fromTo);
    }

    @Override
    protected boolean matchesSafely(T item) {
        if (item == null) {
            return false;
        } else {
            return (item.compareTo(fromTo.getLeft()) > 0) && (item.compareTo(fromTo.getRight()) < 0);
        }
    }

    @Override
    protected void describeMismatchSafely(T item, Description mismatchDescription) {
        mismatchDescription.appendValue(item).appendText(DESC_MISMATCH.toString());
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(DESC_DESCRIPTION.toString());
    }
}
