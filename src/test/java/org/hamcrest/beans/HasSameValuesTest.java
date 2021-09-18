package org.hamcrest.beans;

import com.glowa_net.data.SimplePojo;
import org.hamcrest.AbstractMatcherTest;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.StringDescription;
import org.junit.Before;
import org.junit.Test;

import java.util.regex.Pattern;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThrows;

public class HasSameValuesTest extends AbstractMatcherTest {

    protected static final String  DESCRIPTION_DEFAULT = " descriptionDefault ";
    protected static final String  FIELD_WAS_NULL      = "was null";
    protected static final String  FIELD_NULL          = "null";
    protected static final String  ACTUAL_ITEM_IS_NULL = "actual item is 'null'";
    protected static final Pattern EMPTY_FIELDS        = Pattern.compile(DESCRIPTION_DEFAULT + "\\s*?\\{\\s+\\}", Pattern.MULTILINE + Pattern.DOTALL);

    protected static final String FIELD_SIMPLE_INT    = "simpleInt";
    protected static final String FIELD_SIMPLE_STRING = "simpleString";

    protected static final SimplePojo DEFAULT_POJO         = new SimplePojo();
    protected static final int        DEFAULT_INT_VALUE    = 123;
    protected static final String     DEFAULT_STRING_VALUE = "RELAX";

    protected HasSameValues<SimplePojo> o2T;

    protected static class HasSameValuesTestDifferentClazz {
    }

    @Override
    protected Matcher<?> createMatcher() {
        return HasSameValues.hasSameValues(DEFAULT_POJO);
    }

    @Before
    public void setUp() {
        o2T = (HasSameValues<SimplePojo>) HasSameValues.hasSameValues(DEFAULT_POJO);
    }

    protected Description prepareDescription() {
        return new StringDescription().appendText(DESCRIPTION_DEFAULT);
    }

    protected SimplePojo prepareDifferentObject() {
        final SimplePojo item = new SimplePojo();
        item.setSimpleInt(DEFAULT_INT_VALUE);
        item.setSimpleString(DEFAULT_STRING_VALUE);
        return item;
    }

    protected void verifyDescription(final Description description, final Matcher<String> expected) {
        assertThat(description, not(equalTo(DESCRIPTION_DEFAULT)));
        assertThat(description.toString(), expected);
    }

    protected void verifyThrowable(final Throwable actual, final Matcher<String> expected) {
        assertThat(actual, notNullValue());
        assertThat(actual, instanceOf(AssertionError.class));
        assertThat(actual.getMessage(), expected);
    }

    @Test
    public void testCreates_matcherCreated() {
        assertThat(o2T, notNullValue());
        assertThat(o2T, instanceOf(HasSameValues.class));
    }

    @Test
    public void testMatches_objectsAreTheSame() {
        final SimplePojo item = new SimplePojo();

        assertThat(item, o2T);
    }

    @Test
    public void testMatches_objectsAreDifferent_return_assertError() {
        final SimplePojo item = prepareDifferentObject();

        final Throwable actual = assertThrows(Throwable.class, () -> assertThat(item, o2T));
        verifyThrowable(actual, allOf( //
                containsString(FIELD_SIMPLE_INT + ": expected: <0> but: was <" + DEFAULT_INT_VALUE + ">"), //
                containsString(FIELD_SIMPLE_STRING + ": expected: null but: was \"" + DEFAULT_STRING_VALUE + "\"") //
        ));
    }

    @Test
    public void testMatchesSafely_theSame_return_true() {
        final SimplePojo item = new SimplePojo();

        final boolean actual = o2T.matchesSafely(item);
        assertThat(actual, is(true));
    }

    @Test
    public void testMatchesSafely_withDifference_return_false() {
        final SimplePojo item = new SimplePojo();
        item.setSimpleInt(DEFAULT_INT_VALUE);

        final boolean actual = o2T.matchesSafely(item);
        assertThat(actual, is(false));
    }

    @Test
    public void testDescribeTo_description_isChanged() {
        final Description description = prepareDescription();

        o2T.describeTo(description);
        verifyDescription(description, containsString(HasSameValues.DESC_DESCRIPTION.toString()));
    }

    @Test
    public void testDescribeMismatch_WrongObjectType_description_isChanged() {
        final Description description = prepareDescription();

        final HasSameValuesTestDifferentClazz actual = new HasSameValuesTestDifferentClazz();
        o2T.describeMismatch(actual, description);
        verifyDescription(description, containsString(HasSameValuesTestDifferentClazz.class.getName()));
    }

    @Test
    public void testDescribeMismatchSafely_nullObject_description_isChanged_withNull() {
        final Description description = prepareDescription();

        o2T.describeMismatchSafely(null, description);
        verifyDescription(description, containsString(DESCRIPTION_DEFAULT + FIELD_NULL));
    }

    @Test
    public void testDescribeMismatchSafely_sameObject_description_isChanged() {
        final SimplePojo item = new SimplePojo();
        final Description description = prepareDescription();

        o2T.describeMismatchSafely(item, description);
        verifyDescription(description, Matchers.matchesRegex(EMPTY_FIELDS));
    }

    @Test
    public void testDescribeMismatchSafely_differenObject_description_isChanged() {
        final SimplePojo item = prepareDifferentObject();
        final Description description = prepareDescription();

        o2T.describeMismatchSafely(item, description);
        verifyDescription(description, Matchers.matchesRegex(EMPTY_FIELDS));
    }

}
