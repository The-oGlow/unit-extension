package com.glowanet.util.hamcrest;

import com.glowanet.util.junit.TestResultHelper;
import org.hamcrest.AbstractExtendedMatcherTest;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Base clazz for testing a {@link org.hamcrest.BaseMatcher}
 * For testing extended matcher, use {@link AbstractExtendedMatcherTest}
 *
 * @param <T> the type, the {@code o2T} uses
 *
 * @see AbstractExtendedMatcherTest
 */
public abstract class AbstractMatcherTest<T> {

    /**
     * Clazz for testing a {@link Matcher} with a different clazz.
     */
    protected static class AbstractMatcherTestDifferentClazz {
    }

    /**
     * Clazz for testing a {@link Matcher} with a unknown type.
     */
    protected static class UnknownType {
    }

    protected static final String DIFFERENT_CLAZZ_NAME        = AbstractMatcherTestDifferentClazz.class.getName();
    protected static final String DIFFERENT_CLAZZ_SIMPLE_NAME = AbstractMatcherTestDifferentClazz.class.getSimpleName();
    protected static final String UNKNOWN_TYPE_NAME           = UnknownType.class.getName();

    protected static final String DESCRIPTION_DEFAULT = " descriptionDefault ";
    protected static final String FIELD_WAS_NULL      = "was null";
    protected static final String FIELD_NULL          = "null";
    protected static final String ACTUAL_ITEM_IS_NULL = "actual item is 'null'";

    private final Matcher<T> o2T;
    private final Class<?>   o2TClazz;

    /**
     * Default constructor.
     */
    protected AbstractMatcherTest() {
        this.o2T = createMatcher();
        this.o2TClazz = this.o2T == null ? null : this.o2T.getClass();
    }

    /**
     * Create an instance of {@link #o2T} so some generic tests can be run on it.
     *
     * @return a new {@link Matcher} instance
     *
     * @see #o2T
     * @see #o2T()
     */
    protected abstract Matcher<T> createMatcher();

    /**
     * @return a new {@link Description} instance used in {@link #o2T}
     *
     * @see #o2T
     */
    protected Description prepareDefaultDescription() {
        return new StringDescription().appendText(DESCRIPTION_DEFAULT);
    }

    /**
     * @return a new object used as comparison inside of {@link #o2T}
     *
     * @see #o2T
     * @see #prepareArgumentToCompareWith()
     */
    protected abstract T prepareArgumentInMatcher();

    /**
     * @return a new object with different content as the object inside of {@link #o2T}
     *
     * @see #o2T
     * @see #prepareArgumentInMatcher()
     */
    protected abstract T prepareArgumentToCompareWith();

    /**
     * Fails with the given {@code message}.
     *
     * @param message the text which is printed, when failing
     *
     * @see #fail()
     */
    protected static void fail(String message) {
        Assert.fail(message);
    }

    /**
     * Fails a test with no message.
     *
     * @see #fail(String)
     */
    protected static void fail() {
        Assert.fail();//NOSONAR java:S2698
    }

    /**
     * Expects, that the {@code arg} is matching with the {@code matcher}.
     *
     * @param matcher the {@link Matcher} to test
     * @param arg     the argument to test against the {@code matcher}
     * @param <T>     the type, the {@code matcher} uses
     *
     * @see #assertDoesNotMatch(Matcher, Object)
     */
    protected static <T> void assertMatches(Matcher<T> matcher, T arg) {
        assertMatches("Expected match, but mismatched", matcher, arg);
    }

    /**
     * Expects, that the {@code arg} is matching with the {@code matcher}.
     *
     * @param message an individual fail message
     * @param matcher the {@link Matcher} to test
     * @param arg     the argument to test against the {@code matcher}
     * @param <T>     the type, the {@code matcher} uses
     *
     * @see #assertDoesNotMatch(String, Matcher, Object)
     */
    protected static <T> void assertMatches(String message, Matcher<T> matcher, Object arg) {
        if (!matcher.matches(arg)) {
            fail(String.format("%s, because: '%s'", message, mismatchDescriptionText(matcher, arg)));
        }
    }

    /**
     * Expects, that the {@code mismatchArg} does NOT match with the {@code matcher}.
     *
     * @param matcher     the {@link Matcher} to test
     * @param mismatchArg the argument to test against the {@code matcher}
     * @param <T>         the type, the {@code matcher} uses
     *
     * @see #assertMatches(Matcher, Object)
     */
    protected static <T> void assertDoesNotMatch(Matcher<T> matcher, T mismatchArg) {
        assertDoesNotMatch("Unexpected match", matcher, mismatchArg);
    }

    /**
     * Expects, that the {@code mismatchArg} does NOT match with the {@code matcher}.
     *
     * @param message     an individual fail message
     * @param matcher     the {@link Matcher} to test
     * @param mismatchArg the argument to test against the {@code matcher}
     * @param <T>         the type, the {@code matcher} uses
     *
     * @see #assertMatches(String, Matcher, Object)
     */
    protected static <T> void assertDoesNotMatch(String message, Matcher<T> matcher, T mismatchArg) {
        assertThat(message, matcher.matches(mismatchArg), is(false));
    }

    /**
     * Expects, that the {@code matcher} uses the {@code expectedText} for the {@link Description}.
     *
     * @param expectedText the text used in the the {@link Description} of the {@code matcher}
     * @param matcher      the {@link Matcher} to test
     * @param <T>          the type, the {@code matcher} uses
     *
     * @see #assertMismatchDescription(String, Matcher, Object)
     */
    protected static <T> void assertDescription(String expectedText, Matcher<T> matcher) {
        Description description = new StringDescription();
        description.appendDescriptionOf(matcher);
        assertThat(description, notNullValue());
        assertThat("Expected description", description.toString().trim(), containsString(expectedText));
    }

    /**
     * Expects, that the {@code matcher} uses the {@code expectedText} for the mismatch {@link Description}.
     *
     * @param expectedText the text used in the the mismatch {@link Description} of the {@code matcher}
     * @param matcher      the {@link Matcher} to test
     * @param mismatchArg  an argument which does NOT matches with the {@code matcher}
     * @param <T>          the type, the {@code matcher} uses
     *
     * @see #assertDescription(String, Matcher)
     */
    protected static <T> void assertMismatchDescription(String expectedText, Matcher<? super T> matcher, Object mismatchArg) {
        assertThat("Precondition: Matcher should not match argument.", not(matcher.matches(mismatchArg)));
        assertThat("Expected mismatch description", mismatchDescriptionText(matcher, mismatchArg), containsString(expectedText));
    }

    /**
     * Expects, that the {@code matcher} can handle a NULL argument without exception.
     *
     * @param matcher the {@link Matcher} to test
     * @param <T>     the type, the {@code matcher} uses
     */
    protected static <T> void assertNullSafe(Matcher<T> matcher) {
        try {
            matcher.matches(null);
        } catch (Exception e) {
            fail(String.format("Matcher is not null safe, because: %s", e));
        }
    }

    /**
     * Expects, that the {@code matcher} can handle an argument with a non compatible type without exception.
     *
     * @param matcher the {@link Matcher} to test
     * @param <T>     the type, the {@code matcher} uses
     */
    protected static <T> void assertUnknownTypeSafe(Matcher<T> matcher) {
        try {
            matcher.matches(new UnknownType());
        } catch (Exception e) {
            fail(String.format("Matcher is not safe for unknown types, because: %s", e));
        }
    }

    /**
     * Check, if the {@code actualDescription} is different to the {@link #DESCRIPTION_DEFAULT} and matching with {@code expected}.
     *
     * @param actualDescription the current {@link Description}
     * @param expected          a String-{@link Matcher} to verify the {@code actualDescription}
     *
     * @see #DESCRIPTION_DEFAULT
     */
    protected void verifyDescription(Description actualDescription, Matcher<String> expected) {
        assertThat("expectedMatcher is no valid matcher", expected, instanceOf(Matcher.class));
        assertThat(actualDescription, not(equalTo(DESCRIPTION_DEFAULT)));
        assertThat(actualDescription.toString(), expected);
    }

    /**
     * Check, if the {@code actualException} is an instance of [@link AssertionError} and the message is matching with {@code expected}.
     *
     * @param actualException the current [@link {@link Throwable}
     * @param expected        a String-{@link Matcher} to verify the {@code actualException}
     */
    protected void verifyThrowable(Throwable actualException, Matcher<String> expected) {
        assertThat("expectedMatcher is no valid matcher", expected, instanceOf(Matcher.class));
        assertThat(actualException, notNullValue());
        assertThat(actualException, instanceOf(AssertionError.class));
        assertThat(actualException.getMessage(), expected);
    }

    /**
     * Generates the discription in case the {@code mismatchArg} does not match with the {@code matcher}.
     *
     * @param matcher     the {@link Matcher} to test
     * @param mismatchArg an argument which does NOT matches with the {@code matcher}
     * @param <T>         the type, the {@code matcher} uses
     *
     * @return a text for the missmatch {@link Description}
     */
    private static <T> String mismatchDescriptionText(Matcher<? super T> matcher, Object mismatchArg) {
        Description description = new StringDescription();
        matcher.describeMismatch(mismatchArg, description);
        return description.toString().trim();
    }

    /**
     * Tests, if the matcher is a specific type.
     *
     * @param expectedClazz   the type of matcher
     * @param matcherInstance an matcherInstance to check for
     *
     * @return the {@code expectedClazz} is matching the runtime type of {@code matcherInstance}
     */
    protected static boolean isMatcherType(Class<?> expectedClazz, Matcher<?> matcherInstance) {
        return matcherInstance != null && expectedClazz != null && expectedClazz.isAssignableFrom(matcherInstance.getClass());
    }

    /**
     * @return the {@link #o2T} to test against
     */
    protected Matcher<T> o2T() {
        return this.o2T;
    }

    /**
     * @return the type of {@code o2T}
     */
    protected Class<?> getO2TClazz() {
        return o2TClazz;
    }

    /* Section for {@link org.hamcrest.BaseMatcher} unit tests */

    /**
     * Generic unit test, to assume the {@code o2T} is created.
     */
    @Test
    public void testGeneric_matcher_created() {
        assertThat(o2T, notNullValue());
        assertThat(o2TClazz, notNullValue());
        assertThat(o2T, instanceOf(o2TClazz));
    }

    /**
     * Generic unit test, to assume the {@code o2T} is null-safe.
     */
    @Test
    public void testGeneric_matcher_isNullSafe() {
        assertNullSafe(o2T);
    }

    /**
     * Generic unit test, to assume the {@code o2T} can handle arguments with an unknown type.
     */
    @Test
    public void testGeneric_matcher_isUnknownTypeSafe() {
        assertUnknownTypeSafe(o2T);
    }

    /**
     * Generic unit test, to assume the {@code o2T} identifies an identically object as match.
     */
    @Test
    public void testGeneric_testMatches_objectsAreTheSame_matches() {
        T actual = prepareArgumentInMatcher();
        assertThat(actual, o2T);
    }

    /**
     * @return a matcher, for the use in an unit test
     *
     * @see #testGeneric_testMatches_objectsAreDifferent_throw_assertError()
     */
    protected abstract Matcher<String> prepareMatcher_objectsAreDifferent_check();

    /**
     * Generic unit test, to assume the {@code o2T} identifies a different object with an {@link AssertionError}.
     */
    @Test
    public void testGeneric_testMatches_objectsAreDifferent_throw_assertError() {
        T argument = prepareArgumentToCompareWith();

        Throwable actualThrowable = TestResultHelper.verifyException(() -> assertThat(argument, o2T), Throwable.class);
        verifyThrowable(actualThrowable, prepareMatcher_objectsAreDifferent_check());
    }

    /**
     * @return a {@link Matcher}, for use in an unit test
     *
     * @see #testGeneric_testDescribeTo_descriptionDefault_isAdded()
     */
    protected abstract Matcher<String> prepareMatcherDescriptionText_defaultDescription_check();

    /**
     * Generic unit test, to assume the {@code o2T} adds a default text to the {@link Description}.
     */
    @Test
    public void testGeneric_testDescribeTo_descriptionDefault_isAdded() {
        Description description = prepareDefaultDescription();

        o2T.describeTo(description);
        verifyDescription(description, prepareMatcherDescriptionText_defaultDescription_check());
    }

    /**
     * @return a {@link Matcher}, for use in an unit test
     *
     * @see #testGeneric_testDescribeMismatch_differentObjectContent_description_isAdded()
     */
    protected abstract Matcher<String> prepareDescriptionText_missmatchContent_check();

    /**
     * Generic unit test, to assume the {@code o2T} adds a missmatch text {@link Description}, when object has a different content.
     */
    @Test
    public void testGeneric_testDescribeMismatch_differentObjectContent_description_isAdded() {
        Description description = prepareDefaultDescription();
        T item = prepareArgumentToCompareWith();

        o2T.describeMismatch(item, description);
        verifyDescription(description, prepareDescriptionText_missmatchContent_check());
    }

    /**
     * @return a {@link Matcher}, for use in an unit test
     *
     * @see #testGeneric_testDescribeMismatch_differentObjectType_description_isAdded()
     */
    protected abstract Matcher<String> prepareDescriptionText_missmatchType_check();

    /**
     * Generic unit test, to assume the {@code o2T} adds a missmatch text {@link Description}, when object has a different type.
     */
    @Test
    public void testGeneric_testDescribeMismatch_differentObjectType_description_isAdded() {
        Description description = prepareDefaultDescription();
        AbstractMatcherTestDifferentClazz item = new AbstractMatcherTestDifferentClazz();

        o2T.describeMismatch(item, description);
        verifyDescription(description, prepareDescriptionText_missmatchType_check());
    }
}
