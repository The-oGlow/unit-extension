package org.hamcrest.beans;

import com.glowa_net.data.SimplePojo;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.StringDescription;
import org.junit.Before;
import org.junit.Test;

import java.util.regex.Pattern;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThrows;

public class HasSameValuesTest {

    protected static final String                    DESCRIPTION_DEFAULT = " descriptionDefault ";
    protected static final SimplePojo                DEFAULT_POJO        = new SimplePojo();
    public static final    String                    FIELD_WAS_NULL      = "was null";
    public static final    String                    FIELD_NULL          = "null";
    public static final    String                    ACTUAL_ITEM_IS_NULL = "actual item is 'null'";
    private                HasSameValues<SimplePojo> o2T;
    private static final   Pattern                   EMPTY_FIELDS        = Pattern
            .compile(DESCRIPTION_DEFAULT + "\\s*?\\{\\s+\\}", Pattern.MULTILINE + Pattern.DOTALL);

    private static class HasSameValuesTestClazz {

    }

    @Before
    public void setUp() {
        o2T = (HasSameValues<SimplePojo>) HasSameValues.hasSameValues(DEFAULT_POJO);
    }

    private Description createDescription() {
        return new StringDescription().appendText(DESCRIPTION_DEFAULT);
    }

    private SimplePojo prepareDifferentObject() {
        final SimplePojo item = new SimplePojo();
        item.setSimpleInt(123);
        item.setSimpleString("RELAX");
        return item;
    }

    private void verifyDescription(final Description description, final Matcher<String> expected) {
        assertThat(description, not(equalTo(DESCRIPTION_DEFAULT)));
        assertThat(description.toString(), expected);
    }

    private void verifyThrowable(final Throwable actual, final Matcher<String> expected) {
        assertThat(actual, notNullValue());
        assertThat(actual, instanceOf(AssertionError.class));
        assertThat(actual.getMessage(), expected);
    }

    @Test
    public void testMatchesObjectsAreTheSameReturnsOk() {
        final SimplePojo item = new SimplePojo();

        assertThat(item, o2T);
    }

    @Test
    public void testMatchesObjectsAreDifferentReturnsAssertError() {
        final SimplePojo item = prepareDifferentObject();

        final Throwable actual = assertThrows(Throwable.class, () -> assertThat(item, o2T));
        verifyThrowable(actual, allOf( //
                containsString("simpleInt: expected: <0> but: was <123>"), //
                containsString("simpleString: expected: null but: was \"RELAX\"") //
        ));
    }

    @Test
    public void testCreatesNewMatcher() {
        assertThat(o2T, notNullValue());
        assertThat(o2T, instanceOf(HasSameValues.class));
    }

    @Test
    public void testMatchesSafelyTheSameMatchesTrue() {
        final SimplePojo item = new SimplePojo();

        final boolean actual = o2T.matchesSafely(item);
        assertThat(actual, is(true));
    }

    @Test
    public void testMatchesSafelyOneDifferenceMatchesFalse() {
        final SimplePojo item = new SimplePojo();
        item.setSimpleInt(123);

        final boolean actual = o2T.matchesSafely(item);
        assertThat(actual, is(false));
    }

    @Test
    public void testMatchesSafelyMultipleDifferenceMatchesFalse() {
        final SimplePojo item = prepareDifferentObject();

        final boolean actual = o2T.matchesSafely(item);
        assertThat(actual, is(false));
    }

    @Test
    public void testMatchesSafelyNullMatchesFalse() {
        final Throwable actual = assertThrows(Throwable.class, () -> o2T.matchesSafely(null));
        verifyThrowable(actual, containsStringIgnoringCase(ACTUAL_ITEM_IS_NULL));
    }

    @Test
    public void testDescribeMismatchSafelySameObjectReturnsModfiedDescription() {
        final SimplePojo item = new SimplePojo();
        final Description description = createDescription();

        o2T.describeMismatchSafely(item, description);
        verifyDescription(description, Matchers.matchesRegex(EMPTY_FIELDS));
    }

    @Test
    public void testDescribeMismatchSafelyDifferenObjectReturnsModfiedDescription() {
        final SimplePojo item = prepareDifferentObject();
        final Description description = createDescription();

        o2T.describeMismatchSafely(item, description);
        verifyDescription(description, Matchers.matchesRegex(EMPTY_FIELDS));
    }

    @Test
    public void testDescribeMismatchSafelyNullObjectReturnsNullValueDescription() {
        final Description description = createDescription();

        o2T.describeMismatchSafely(null, description);
        verifyDescription(description, containsString(DESCRIPTION_DEFAULT + FIELD_NULL));
    }

    @Test
    public void testDescribeMismatchSafelyNullDescriptionThrowsNPE() {
        assertThrows(NullPointerException.class, () -> o2T.describeMismatchSafely(DEFAULT_POJO, null));
    }

    @Test
    public void testDescribeMismatchSameObjectModfiesDescription() {
        final SimplePojo item = DEFAULT_POJO;
        final Description description = createDescription();

        o2T.describeMismatch(item, description);
        verifyDescription(description, Matchers.matchesRegex(EMPTY_FIELDS));
    }

    @Test
    public void testDescribeMismatchDifferentObjectModfiesDescription() {
        final SimplePojo item = prepareDifferentObject();
        final Description description = createDescription();

        o2T.describeMismatch(item, description);
        verifyDescription(description, Matchers.matchesRegex(EMPTY_FIELDS));
    }

    @Test
    public void testDescribeMismatchNullObjectReturnsNullValueDescription() {
        final Description description = createDescription();

        o2T.describeMismatch(null, description);
        verifyDescription(description, containsString(DESCRIPTION_DEFAULT + FIELD_WAS_NULL));
    }

    @Test
    public void testDescribeMismatchNullDescriptionThrowsNPE() {
        assertThrows(NullPointerException.class, () -> o2T.describeMismatch(DEFAULT_POJO, null));
    }

    @Test
    public void testDescribeMismatchWrongObjectTypeReturnsModfiedDescription() {
        final Description description = createDescription();

        final HasSameValuesTestClazz actual = new HasSameValuesTestClazz();
        o2T.describeMismatch(actual, description);
        verifyDescription(description, containsString(HasSameValuesTestClazz.class.getName()));
    }

    @Test
    public void testDescribeToReturnsModifiedDescription() {
        final Description description = createDescription();

        o2T.describeTo(description);
        verifyDescription(description, containsString(HasSameValues.DESC_DESCRIPTION.toString()));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void testDescribeToNullValueDescriptionThrowsNPE() {
        o2T.describeTo(null);
    }

}
