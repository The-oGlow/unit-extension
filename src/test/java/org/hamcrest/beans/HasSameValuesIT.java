package org.hamcrest.beans;

import com.glowa_net.data.SimplePojo;
import org.hamcrest.Description;
import org.hamcrest.Matchers;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThrows;

public class HasSameValuesIT extends HasSameValuesTest {

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void testDescribeTo_withNullValue_throw_NPE() {
        o2T.describeTo(null);
    }

    @Test
    public void testDescribeMismatch_withSameObject_description_isChanged() {
        final SimplePojo item = DEFAULT_POJO;
        final Description description = prepareDescription();

        o2T.describeMismatch(item, description);
        verifyDescription(description, Matchers.matchesRegex(EMPTY_FIELDS));
    }

    @Test
    public void testDescribeMismatch_withDifferentObject_description_isChanged() {
        final SimplePojo item = prepareDifferentObject();
        final Description description = prepareDescription();

        o2T.describeMismatch(item, description);
        verifyDescription(description, Matchers.matchesRegex(EMPTY_FIELDS));
    }

    @Test
    public void testDescribeMismatch_withNullObject_description_isChanged_withNull() {
        final Description description = prepareDescription();

        o2T.describeMismatch(null, description);
        verifyDescription(description, containsString(DESCRIPTION_DEFAULT + FIELD_WAS_NULL));
    }

    @Test
    public void testDescribeMismatch_withNullDescription_throw_NPE() {
        assertThrows(NullPointerException.class, () -> o2T.describeMismatch(DEFAULT_POJO, null));
    }

    @Test
    public void testMatchesSafely_multipleDifference_return_false() {
        final SimplePojo item = prepareDifferentObject();

        final boolean actual = o2T.matchesSafely(item);
        assertThat(actual, is(false));
    }

    @Test
    public void testMatchesSafely_withNull_return_false() {
        final Throwable actual = assertThrows(Throwable.class, () -> o2T.matchesSafely(null));
        verifyThrowable(actual, containsStringIgnoringCase(ACTUAL_ITEM_IS_NULL));
    }

    @Test
    public void testDescribeMismatchSafely_withNullDescription_throw_NPE() {
        assertThrows(NullPointerException.class, () -> o2T.describeMismatchSafely(DEFAULT_POJO, null));
    }

}
