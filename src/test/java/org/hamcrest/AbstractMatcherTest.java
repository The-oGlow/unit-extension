package org.hamcrest;

import junit.framework.TestCase;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

public abstract class AbstractMatcherTest extends TestCase {

    /**
     * Create an instance of the Matcher so some generic safety-net tests can be run on it.
     */
    protected abstract Matcher<?> createMatcher();

    public static <T> void assertMatches(Matcher<T> matcher, T arg) {
        assertMatches("Expected match, but mismatched", matcher, arg);
    }

    public static <T> void assertMatches(String message, Matcher<T> matcher, Object arg) {
        if (!matcher.matches(arg)) {
            fail(message + " because: '" + mismatchDescription(matcher, arg) + "'");
        }
    }

    public static <T> void assertDoesNotMatch(Matcher<? super T> c, T arg) {
        assertDoesNotMatch("Unexpected match", c, arg);
    }

    public static <T> void assertDoesNotMatch(String message, Matcher<? super T> c, T arg) {
        assertThat(message, c.matches(arg), is(false));
    }

    public static void assertDescription(String expectedText, Matcher<?> matcher) {
        Description description = new StringDescription();
        description.appendDescriptionOf(matcher);
        assertThat("Expected description", description.toString().trim(), containsString(expectedText));
    }

    public static <T> void assertMismatchDescription(String expectedText, Matcher<? super T> matcher, Object actual) {
        assertThat("Precondition: Matcher should not match item.", not(matcher.matches(actual)));
        assertThat("Expected mismatch description", mismatchDescription(matcher, actual), containsString(expectedText));
    }

    public static void assertNullSafe(Matcher<?> matcher) {
        try {
            matcher.matches(null);
        } catch (Exception e) {
            fail("Matcher was not null safe");
        }
    }

    public static void assertUnknownTypeSafe(Matcher<?> matcher) {
        try {
            matcher.matches(new UnknownType());
        } catch (Exception e) {
            fail("Matcher was not unknown type safe, because: " + e);
        }
    }

    public void testIsNullSafe() {
        assertNullSafe(createMatcher());
    }

    public void testCopesWithUnknownTypes() {
        createMatcher().matches(new UnknownType());
    }

    private static <T> String mismatchDescription(Matcher<? super T> matcher, Object arg) {
        Description description = new StringDescription();
        matcher.describeMismatch(arg, description);
        return description.toString().trim();
    }

    @SuppressWarnings("WeakerAccess")
    public static class UnknownType {
    }

}
