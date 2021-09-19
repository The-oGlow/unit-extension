package org.hamcrest.beans;

import com.glowa_net.data.SimplePojo;
import org.hamcrest.AbstractMatcherTest;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.regex.Pattern;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;

public class HasSameValuesTest<T extends SimplePojo> extends AbstractMatcherTest<T> {

    protected static final Pattern EMPTY_FIELDS = Pattern.compile(DESCRIPTION_DEFAULT + "\\s*?\\{\\s+\\}", Pattern.MULTILINE + Pattern.DOTALL);

    protected static final String FIELD_SIMPLE_INT    = "simpleInt";
    protected static final String FIELD_SIMPLE_STRING = "simpleString";

    protected static final int    DEFAULT_INT      = 123;
    protected static final String DEFAULT_STRING   = "RELAX";
    protected static final int    DIFFERENT_INT    = 789;
    protected static final String DIFFERENT_STRING = "DON'T DO IT";

    @Override
    protected Matcher<T> createMatcher() {
        return HasSameValues.hasSameValues(prepareMatcherArgument());
    }

    @Override
    protected T prepareMatcherArgument() {
        T item = (T) new SimplePojo();
        item.setSimpleInt(DEFAULT_INT);
        item.setSimpleString(DEFAULT_STRING);
        return item;
    }

    @Override
    protected T prepareComparingArgument() {
        T item = (T) new SimplePojo();
        item.setSimpleInt(DIFFERENT_INT);
        item.setSimpleString(DIFFERENT_STRING);
        return item;
    }

    @Override
    protected Matcher<String> prepareObjectsAreDifferentCheck() {
        return allOf( //
                containsString(FIELD_SIMPLE_INT + ": expected: <0> but: was <" + DIFFERENT_INT + ">"), //
                containsString(FIELD_SIMPLE_STRING + ": expected: null but: was \"" + DIFFERENT_STRING + "\"") //
        );
    }

    @Override
    protected Matcher<String> prepareDescriptionTextDefaultCheck() {
        return containsString(HasSameValues.DESC_DESCRIPTION.toString());
    }

    @Override
    protected Matcher<String> prepareDescriptionTextMissmatchContentCheck() {
        return containsString(DESCRIPTION_DEFAULT);
    }

    @Override
    protected Matcher<String> prepareDescriptionTextMissmatchTypeCheck() {
        return containsString(AbstractMatcherTestDifferentClazz.class.getName());
    }

    @Override
    protected Matcher<String> prepareDescriptionTextMissmatchSafelySameObjectCheck() {
        return Matchers.matchesRegex(EMPTY_FIELDS);
    }

    @Override
    protected Matcher<String> prepareDescriptionTextMissmatchSafelyDifferentObjectCheck() {
        return Matchers.matchesRegex(EMPTY_FIELDS);
    }

    @Override
    protected Matcher<String> prepareDescriptionTextMissmatchSafelyNullObjectCheck() {
        return containsString(DESCRIPTION_DEFAULT + FIELD_NULL);
    }

    @Test
    public void testMatchesSafely_noGetter_throw_IAE() {

    }

//    @Test
//    public void testCreates_matcherCreated() {
//        assertThat(tsO2T(), notNullValue());
//        assertThat(tsO2T(), instanceOf(HasSameValues.class));
//    }

//    @Test
//    public void testMatches_objectsAreTheSame() {
//        T actual = prepareMatcherObject();
//        assertThat(actual, tsO2T());
//    }

//    @Test
//    public void testMatches_objectsAreDifferent_return_assertError() {
//        T item = prepareComparingArgument();
//
//        Matcher<String> expected = allOf( //
//                containsString(FIELD_SIMPLE_INT + ": expected: <0> but: was <" + DEFAULT_INT_VALUE + ">"), //
//                containsString(FIELD_SIMPLE_STRING + ": expected: null but: was \"" + DEFAULT_STRING_VALUE + "\"") //
//        );
//
//        final Throwable actual = assertThrows(Throwable.class, () -> assertThat(item, tsO2T()));
//        verifyThrowable(actual, expected);
//    }

//    @Test
//    public void testMatchesSafely_theSame_return_true() {
//        T item = prepareMatcherArgument();
//        HasSameValues<T> tsO2T = tsO2T();
//
//        final boolean actual = tsO2T.matchesSafely(item);
//        assertThat(actual, is(true));
//    }

//    @Test
//    public void testMatchesSafely_withDifference_return_false() {
//        T item = prepareMatcherArgument();
//        item.setSimpleInt(DEFAULT_INT_VALUE);
//        HasSameValues<T> tsO2T = tsO2T();
//
//        final boolean actual = tsO2T.matchesSafely(item);
//        assertThat(actual, is(false));
//    }


//    @Test
//    public void testDescribeTo_description_isChanged() {
//        final Description description = prepareDescription();
//
//        o2T().describeTo(description);
//        verifyDescription(description, containsString(HasSameValues.DESC_DESCRIPTION.toString()));
//    }

//    @Test
//    public void testDescribeMismatch_WrongObjectType_description_isChanged() {
//        final Description description = prepareDescription();
//
//        final HasSameValuesTestDifferentClazz actual = new HasSameValuesTestDifferentClazz();
//        o2T().describeMismatch(actual, description);
//        verifyDescription(description, containsString(HasSameValuesTestDifferentClazz.class.getName()));
//    }

//    @Test
//    public void testDescribeMismatchSafely_nullObject_description_isChanged_withNull() {
//        final Description description = prepareDescription();
//        HasSameValues<T> tsO2T = tsO2T();
//
//        tsO2T.describeMismatchSafely(null, description);
//        verifyDescription(description, containsString(DESCRIPTION_DEFAULT + FIELD_NULL));
//    }

    //    @Test
//    public void testDescribeMismatchSafely_sameObject_description_isChanged() {
//        final T item = prepareMatcherArgument();
//        final Description description = prepareDescription();
//        HasSameValues<T> tsO2T = tsO2T();
//
//        tsO2T.describeMismatchSafely(item, description);
//        verifyDescription(description, Matchers.matchesRegex(EMPTY_FIELDS));
//    }

//    @Test
//    public void testDescribeMismatchSafely_differenObject_description_isChanged() {
//        final T item = prepareComparingArgument();
//        final Description description = prepareDescription();
//        HasSameValues<T> tsO2T = tsO2T();
//
//        tsO2T.describeMismatchSafely(item, description);
//        verifyDescription(description, Matchers.matchesRegex(EMPTY_FIELDS));
//    }

}
