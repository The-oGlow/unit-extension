package org.hamcrest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.notNullValue;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.hamcrest.core.IsBetween;
import org.hamcrest.core.IsBetweenWithBound;
import org.junit.Before;
import org.junit.Test;

public class BetweenMatcherTest {

    private static final Integer                from  = 10;
    private static final Integer                to    = 100;
    private final        Pair<Integer, Integer> range = new ImmutablePair<>(from, to);

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testBetween() {
        Matcher<Integer> actual = BetweenMatcher.between(from, to);
        verifyMatcher(actual, IsBetween.class);
    }

    @Test
    public void testBetweenWithBound() {
        Matcher<Integer> actual = BetweenMatcher.betweenWithBound(from, to);
        verifyMatcher(actual, IsBetweenWithBound.class);
    }

    @Test
    public void testBetweenWithRange() {
        Matcher<Integer> actual = BetweenMatcher.between(range);
        verifyMatcher(actual, IsBetween.class);
    }

    @Test
    public void testBetweenWithBoundWithRange() {
        Matcher<Integer> actual = BetweenMatcher.betweenWithBound(range);
        verifyMatcher(actual, IsBetween.class);
    }

    private void verifyMatcher(Matcher<?> actual, Class<?> expectedClazz) {
        assertThat(actual, notNullValue());
        assertThat(actual, instanceOf(expectedClazz));
    }
}
