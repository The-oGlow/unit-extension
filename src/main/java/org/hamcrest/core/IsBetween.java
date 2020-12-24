package org.hamcrest.core;

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
 * @param <T> the type of the class which will be checked
 *
 * @author Oliver Glowa
 * @since 0.01.000
 */
public class IsBetween<T extends Comparable<T>> extends TypeSafeMatcher<T> {

    static final Description DESC_MISMATCH    = new StringDescription();
    static final Description DESC_DESCRIPTION = new StringDescription();

    protected final T from;
    protected final T to;

    protected IsBetween(final T from, final T to) {
        super();
        verifyInput(from, to);

        this.from = from;
        this.to = to;

        initDescription();
    }

    protected void verifyInput(final T from, final T to) {
        assertThat(from, notNullValue());
        assertThat(to, notNullValue());
        assertThat(to, greaterThan(from));
    }

    protected void initDescription() {
        DESC_DESCRIPTION.appendText("The value must be between ").appendValue(from).appendText(" and ").appendValue(to)
                .appendText(". Range start and end not included. ");
        DESC_MISMATCH.appendText(" is not between ").appendValue(from).appendText(" and ").appendValue(to).appendText(". Range start and end not included.");
    }

    public static <T extends Comparable<T>> Matcher<T> between(final T from, final T to) {
        return new IsBetween<>(from, to);
    }

    @Override
    protected boolean matchesSafely(final T item) {
        if (item == null) {
            return false;
        } else {
            return (item.compareTo(from) > 0) && (item.compareTo(to) < 0);
        }
    }

    @Override
    protected void describeMismatchSafely(final T item, final Description mismatchDescription) {
        mismatchDescription.appendValue(item).appendText(DESC_MISMATCH.toString());
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(DESC_DESCRIPTION.toString());
    }
}
