package org.hamcrest.core;

import org.hamcrest.Description;
import org.hamcrest.StringDescription;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThrows;

public class IsBetweenTest {

    protected static final String DESCRIPTION_DEFAULT = " descriptionDefault ";
    protected static final Long   LBOUND              = 1L;
    protected static final Long   UBOUND              = 10L;

    protected IsBetween<Long> o2T;

    private static class IsBetweenTestClazz {
        public IsBetweenTestClazz() {
        }

        ;
    }

    @Before
    public void setUp() throws Exception {
        o2T = (IsBetween<Long>) IsBetween.between(LBOUND, UBOUND);
    }

    public Description createDescription() {
        return new StringDescription().appendText(DESCRIPTION_DEFAULT.toString());
    }

    @Test
    public void testCreatesNewMatcher() {
        assertThat(o2T, notNullValue());
    }

    @Test
    public void testMatchesSafelyInRange1ReturnsTrue() {
        Long item2T = LBOUND + 1;

        boolean actual = o2T.matchesSafely(item2T);
        assertThat(actual, is(true));
    }

    @Test
    public void testMatchesSafelyInRange2ReturnsTrue() {
        Long item2T = UBOUND - 1;

        boolean actual = o2T.matchesSafely(item2T);
        assertThat(actual, is(true));
    }

    @Test
    public void testMatchesSafelyTooSmallReturnsFalse() {
        Long item2T = LBOUND;

        boolean actual = o2T.matchesSafely(item2T);
        assertThat(actual, is(false));
    }

    @Test
    public void testMatchesSafelyTooBigReturnsFalse() {
        Long item2T = UBOUND;

        boolean actual = o2T.matchesSafely(item2T);
        assertThat(actual, is(false));
    }

    @Test
    public void testMatchesSafelyNullValueReturnsFalse() {

        boolean actual = o2T.matchesSafely(null);
        assertThat(actual, is(false));
    }

    @Test
    public void testDescribeMismatchSafelyModfiesDescription() {
        Long actual = LBOUND;
        Description description = createDescription();

        o2T.describeMismatchSafely(actual, description);
        assertThat(description, not(equalTo(DESCRIPTION_DEFAULT)));
        assertThat(description.toString(), containsString(IsBetween.DESC_MISMATCH.toString()));
    }

    @Test
    public void testDescribeMismatchSafelyNullValueDescription() {
        assertThrows(NullPointerException.class, () -> o2T.describeMismatchSafely(LBOUND, null));
    }

    @Test
    public void testDescribeMismatchSafelyNullValueReturnsNullValueDescription() {
        Description description = createDescription();

        o2T.describeMismatchSafely(null, description);
        assertThat(description, not(equalTo(DESCRIPTION_DEFAULT)));
        assertThat(description.toString(), containsString(DESCRIPTION_DEFAULT + "null"));
    }

    @Test
    public void testDescribeMismatchWrongTypeModfiesDescription() {
        Description description = createDescription();

        IsBetweenTestClazz actual = new IsBetweenTestClazz();
        o2T.describeMismatch(actual, description);
        assertThat(description, not(equalTo(DESCRIPTION_DEFAULT)));
        assertThat(description.toString(), containsString(IsBetweenTestClazz.class.getName()));
    }

    @Test
    public void testDescribeToModifiesDescription() {
        Description description = createDescription();

        o2T.describeTo(description);
        assertThat(description, not(equalTo(DESCRIPTION_DEFAULT)));
        assertThat(description.toString(), containsString(IsBetween.DESC_DESCRIPTION.toString()));
    }

    @Test(expected = NullPointerException.class)
    public void testDescribeToNullValueDescription() {
        o2T.describeTo(null);
    }
}
