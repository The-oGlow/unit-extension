package org.hamcrest;

import com.glowanet.annotation.ExcludeFromTesting;
import com.glowanet.util.hamcrest.AbstractMatcherTest;
import com.glowanet.util.junit.rules.ExcludeFromTestingRule;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Rule;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assume.assumeThat;

/**
 * Base clazz for the use of testing extended matcher, like
 * <ul>
 *     <li>{@link org.hamcrest.DiagnosingMatcher}</li>
 *     <li>{@link org.hamcrest.TypeSafeMatcher}</li>
 * </ul>.
 * For testing a standard matcher, use {@link com.glowanet.util.hamcrest.AbstractMatcherTest}.
 *
 * @param <T> the type, the {@code o2T} uses
 *
 * @see org.hamcrest.DiagnosingMatcher
 * @see org.hamcrest.TypeSafeMatcher
 */
public abstract class AbstractExtendedMatcherTest<T> extends AbstractMatcherTest<T> {

    @Rule
    public ExcludeFromTestingRule excludeFromTestingRule = new ExcludeFromTestingRule();

    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * @return the {@code o2T} as {@link TypeSafeMatcher} to test against
     *
     * @see #o2T()
     * @see #dmO2T()
     */
    protected <M extends TypeSafeMatcher<T>> M tsO2T() {
        return (M) o2T();
    }

    /**
     * @return the {@code o2T} as {@link DiagnosingMatcher} to test against
     *
     * @see #o2T()
     * @see #tsO2T()
     */
    protected <M extends DiagnosingMatcher<T>> M dmO2T() {
        return (M) o2T();
    }

    /**
     * Call to assume that {@link #o2T()} satisfies the condition that is a {@link TypeSafeMatcher}.
     *
     * @return
     */
    public boolean assumeIsTypeSafeMatcher() {
        LOGGER.debug("No TypeSafeMatcher available!");
        return !isMatcherType(TypeSafeMatcher.class, o2T());
        //assumeThat("No TypeSafeMatcher available!", isMatcherType(TypeSafeMatcher.class, o2T()), is(true));
    }

    /**
     * Call to assume that {@link #o2T()} satisfies the condition that is a {@link DiagnosingMatcher}.
     */
    public void assumeIsDiagnosingMatcher() {
        assumeThat("No DiagnosingMatcher available!", isMatcherType(DiagnosingMatcher.class, o2T()), is(true));
    }

    /* Section for {@link org.hamcrest.TypeSafeMatcher} unit tests */

    @ExcludeFromTesting(assumeTrue = "assumeIsTypeSafeMatcher")
    @Test
    public void testGeneric_testMatchesSafely_objectsAreTheSame_return_true() {
        //assumeIsTypeSafeMatcher();
        TypeSafeMatcher<T> tsO2T = tsO2T();

        T item = prepareArgumentInMatcher();

        boolean actual = tsO2T.matchesSafely(item);
        assertThat(actual, is(true));
    }

    @ExcludeFromTesting(assumeTrue = "assumeIsTypeSafeMatcher")
    @Test
    public void testGeneric_testMatchesSafely_objectsAreDifferent_return_false() {
        //assumeIsTypeSafeMatcher();
        TypeSafeMatcher<T> tsO2T = tsO2T();

        T item = prepareArgumentToCompareWith();

        boolean actual = tsO2T.matchesSafely(item);
        assertThat(actual, is(false));
    }

    /**
     * @return a {@link Matcher}, for use in an unit test
     *
     * @see #testGeneric_testDescribeMismatchSafely_sameObject_description_notChanged()
     */
    protected abstract Matcher<String> prepareMatcherDescriptionText_missmatchSafely_sameObject_check();

    @ExcludeFromTesting(assumeTrue = "assumeIsTypeSafeMatcher")
    @Test
    public void testGeneric_testDescribeMismatchSafely_sameObject_description_notChanged() {
        //assumeIsTypeSafeMatcher();
        TypeSafeMatcher<T> tsO2T = tsO2T();

        Description description = prepareDefaultDescription();
        T item = prepareArgumentInMatcher();

        tsO2T.describeMismatchSafely(item, description);
        verifyDescription(description, prepareMatcherDescriptionText_missmatchSafely_sameObject_check());
    }

    /**
     * @return a {@link Matcher}, for use in an unit test
     *
     * @see #testGeneric_testDescribeMismatchSafely_differenObject_description_isAdded()
     */
    protected abstract Matcher<String> prepareMatcherDescriptionText_missmatchSafely_differentObject_check();

    @ExcludeFromTesting(assumeTrue = "assumeIsTypeSafeMatcher")
    @Test
    public void testGeneric_testDescribeMismatchSafely_differenObject_description_isAdded() {
        //assumeIsTypeSafeMatcher();
        TypeSafeMatcher<T> tsO2T = tsO2T();

        Description description = prepareDefaultDescription();
        T item = prepareArgumentToCompareWith();

        tsO2T.describeMismatchSafely(item, description);
        verifyDescription(description, prepareMatcherDescriptionText_missmatchSafely_differentObject_check());
    }

    /**
     * @return a {@link Matcher}, for use in an unit test
     *
     * @see #testGeneric_testDescribeMismatchSafely_nullObject_description_isAddedWithNull()
     */
    protected abstract Matcher<String> prepareMatcherDescriptionText_missmatchSafely_nullObject_check();

    @ExcludeFromTesting(assumeTrue = "assumeIsTypeSafeMatcher")
    @Test
    public void testGeneric_testDescribeMismatchSafely_nullObject_description_isAddedWithNull() {
        //assumeIsTypeSafeMatcher();
        TypeSafeMatcher<T> tsO2T = tsO2T();

        Description description = prepareDefaultDescription();
        T item = null;

        tsO2T.describeMismatchSafely(item, description);
        verifyDescription(description, prepareMatcherDescriptionText_missmatchSafely_nullObject_check());
    }

    /* Section for {@link org.hamcrest.DiagnosingMatcher} unit tests */

/*
    @Test
    public void testGeneric_testDescribeTo_nullObject_description_isAddedWithNull() {
        assumeIsDiagnosingMatcher();
        DiagnosingMatcher<T> dmO2T = dmO2T();

        Description description = prepareDefaultDescription();

        dmO2T.describeTo(description);
        verifyDescription(description, null);
    }
*/
}