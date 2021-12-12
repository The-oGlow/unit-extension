package org.hamcrest;

import com.glowanet.util.hamcrest.AbstractPublicTest;
import org.hamcrest.core.IsBetween;
import org.hamcrest.core.IsBetweenWithBound;
import org.junit.Test;

/**
 * @see BetweenMatcher
 */
public class BetweenMatcherTest extends AbstractPublicTest {

    @Test
    public void testBetween_return_aMatcher() {
        actual = BetweenMatcher.between(from, to);
        verifyMatcher(IsBetween.class);
    }

    @Test
    public void testBetweenWithBound_return_aMatcher() {
        actual = BetweenMatcher.betweenWithBound(from, to);
        verifyMatcher(IsBetweenWithBound.class);
    }

    @Test
    public void testBetweenWithRange_return_aMatcher() {
        actual = BetweenMatcher.between(rangeFromTo);
        verifyMatcher(IsBetween.class);
    }

    @Test
    public void testBetweenWithBoundAndRange_return_aMatcher() {
        actual = BetweenMatcher.betweenWithBound(rangeFromTo);
        verifyMatcher(IsBetween.class);
    }
}
