package org.hamcrest.core;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class IsBetweenWithBoundTest extends IsBetweenTest {

    @Override
    @Before
    public void setUp() throws Exception {
        o2T = (IsBetween<Long>) IsBetweenWithBound.between(LBOUND, UBOUND);
    }

    @Override
    @Test
    public void testMatchesSafelyTooSmallReturnsFalse() {
        Long item2T = LBOUND - 1;

        boolean actual = o2T.matches(item2T);
        assertThat(actual, is(false));
    }

    @Override
    @Test
    public void testMatchesSafelyTooBigReturnsFalse() {
        Long item2T = UBOUND + 1;

        boolean actual = o2T.matches(item2T);
        assertThat(actual, is(false));
    }

}
