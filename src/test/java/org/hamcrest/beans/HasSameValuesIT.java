package org.hamcrest.beans;

import com.glowa_net.data.SimplePojo;
import org.hamcrest.Description;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThrows;

public class HasSameValuesIT<T extends SimplePojo> extends HasSameValuesTest<T> {

    @Test
    public void testDescribeTo_withNullValue_throw_NPE() {
        Throwable actual = Assert.assertThrows(Throwable.class, () -> o2T().describeTo(null));
        assertThat(actual, instanceOf(NullPointerException.class));
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
    public void testDescribeMismatch_withNullDescription_throw_NPE() {
        assertThrows(NullPointerException.class, () -> o2T().describeMismatch(prepareArgumentInMatcher(), null));
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
        final Throwable actual = assertThrows(Throwable.class, () -> tsO2T.matchesSafely(null));
        verifyThrowable(actual, containsStringIgnoringCase(ACTUAL_ITEM_IS_NULL));
    }

    @Test
    public void testDescribeMismatchSafely_withNullDescription_throw_NPE() {
        HasSameValues<T> tsO2T = tsO2T();
        assertThrows(NullPointerException.class, () -> tsO2T.describeMismatchSafely(prepareArgumentInMatcher(), null));
    }

}
