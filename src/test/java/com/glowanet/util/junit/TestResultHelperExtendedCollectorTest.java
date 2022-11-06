package com.glowanet.util.junit;

import com.glowanet.util.junit.rules.ErrorCollectorExt;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class TestResultHelperExtendedCollectorTest extends TestResultHelperErrorCollectorTest {

    @Override
    public ErrorCollector prepErrorCollector() {
        return new ErrorCollectorExt();
    }

    @Test
    public void testIsExtendeCollector() {
        MatcherAssert.assertThat(prepErrorCollector(), Matchers.instanceOf(ErrorCollectorExt.class));
    }

    @Override
    @Test
    public void testVerifyCollectorWithMatcher() {
        verifyResult(() -> TestResultHelper.verifyCollector(prepErrorCollector(), MATCHER_EQ_0));
    }

    @Override
    @Test
    public void testVerifyCollectorWithResetWithMatcher() {
        ErrorCollector collector = prepErrorCollector(ERRSIZE_1);

        verifyResult(() -> TestResultHelper.verifyCollectorWithReset(collector, MATCHER_EQ_1), IllegalArgumentException.class);
    }

    @Override
    @Test
    public void testIsExtendIsFalse() {
        boolean actual = TestResultHelper.isExtend(prepErrorCollector());

        assertThat(actual, equalTo(true));
    }

}
