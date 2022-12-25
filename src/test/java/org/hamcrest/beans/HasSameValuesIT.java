package org.hamcrest.beans;

import com.glowanet.data.SimplePojo;
import com.glowanet.util.junit.TestResultHelper;
import org.hamcrest.Description;
import org.hamcrest.Matchers;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.hamcrest.Matchers.is;

public class HasSameValuesIT<T extends SimplePojo> extends HasSameValuesTest<T> {

    @Test
    public void testDescribeTo_withNullValue_throw_NPE() {
        TestResultHelper.verifyException(() -> o2T().describeTo(null), NullPointerException.class);
    }

    @Test
    public void testDescribeMismatch_withSameObject_description_isChanged() {
        final T item = prepareArgumentInMatcher();
        final Description description = prepareDefaultDescription();

        o2T().describeMismatch(item, description);
        verifyDescription(description, Matchers.matchesRegex(EMPTY_FIELDS));
    }

    @Test
    public void testDescribeMismatch_withDifferentObject_description_isChanged() {
        final T item = prepareArgumentToCompareWith();
        final Description description = prepareDefaultDescription();

        o2T().describeMismatch(item, description);
        verifyDescription(description, Matchers.matchesRegex(EMPTY_FIELDS));
    }

    @Test
    public void testDescribeMismatch_withNullObject_description_isChanged_withNull() {
        final Description description = prepareDefaultDescription();

        o2T().describeMismatch(null, description);
        verifyDescription(description, containsString(DESCRIPTION_DEFAULT + FIELD_WAS_NULL));
    }

    @Test
    @SuppressWarnings("java:S5778")
    public void testDescribeMismatch_withNullDescription_throw_NPE() {
        TestResultHelper.verifyException(() -> o2T().describeMismatch(prepareArgumentInMatcher(), null), NullPointerException.class);
    }

    @Test
    public void testMatchesSafely_multipleDifference_return_false() {
        final T item = prepareArgumentToCompareWith();
        HasSameValues<T> tsO2T = tsO2T();

        final boolean actual = tsO2T.matchesSafely(item);
        assertThat(actual, is(false));
    }

    @Test
    public void testMatchesSafely_withNull_return_false() {
        HasSameValues<T> tsO2T = tsO2T();
        final Throwable actual = TestResultHelper.verifyException(() -> tsO2T.matchesSafely(null), Throwable.class);
        verifyThrowable(actual, containsStringIgnoringCase(ACTUAL_ITEM_IS_NULL));
    }

    @Test
    @SuppressWarnings("java:S5778")
    public void testDescribeMismatchSafely_withNullDescription_throw_NPE() {
        HasSameValues<T> tsO2T = tsO2T();
        TestResultHelper.verifyException(() -> tsO2T.describeMismatchSafely(prepareArgumentInMatcher(), null), NullPointerException.class);
    }

}
