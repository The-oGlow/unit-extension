package org.hamcrest.core;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.Before;
import org.junit.Test;
import org.junit.function.IThrowingRunnable;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.not;

@RunWith(Parameterized.class)
public class FailWithTest {

    @Parameterized.Parameters(name = "{index} runnable={0}, exception={1}, expected={2}")
    public static List<Object[]> data() {
        List<Object[]> testData = new ArrayList<>();

        testData.add(new Object[]{
                (IThrowingRunnable<?>) () -> FailWithHelper.getInstance().failWithValidException(),
                FailWithHelper.FailWithValidException.class,
                true
        });
        testData.add(new Object[]{
                (IThrowingRunnable<?>) () -> FailWithHelper.getInstance().failWithInValidException(),
                FailWithHelper.FailWithValidException.class,
                false
        });
        testData.add(new Object[]{
                (IThrowingRunnable<?>) () -> FailWithHelper.getInstance().failWithNoException(),
                FailWithHelper.FailWithValidException.class,
                false
        });
        testData.add(new Object[]{
                null,
                FailWithHelper.FailWithValidException.class,
                false
        });
        return testData;
    }

    @Parameterized.Parameter
    public IThrowingRunnable<?> actualThrowingRunnable;

    @Parameterized.Parameter(1)
    public Class<?> expectedExceptionClazz;

    @Parameterized.Parameter(2)
    public boolean expectedResult;

    private FailWith<?> o2t;

    @Before
    public void setUp() throws Exception {
        o2t = new FailWith<>(FailWithHelper.FailWithValidException.class);
    }

    @Test
    public void testFailWith() {
        Matcher<?> actual = FailWith.failWith(FailWithHelper.FailWithValidException.class);
        assertThat(actual, instanceOf(FailWith.class));
    }

    @Test
    public void testMatches() {
        boolean actual = o2t.matches(actualThrowingRunnable);
        assertThat(actual, equalTo(expectedResult));
    }

    @Test
    public void testGetExceptionClazz() {
        Class<?> actual = o2t.getExceptionClazz();
        assertThat(actual, equalTo(expectedExceptionClazz));
    }

    @Test
    public void testGetExpectedMatcher() {
        Matcher<?> actual = o2t.getExpectedMatcher();
        assertThat(actual, instanceOf(IsInstanceOf.class));
    }

    @Test
    public void testDescribeTo() {
        Description description = new StringDescription();
        assertThat(description.toString(), emptyString());

        o2t.describeTo(description);
        assertThat(description.toString(), not(emptyString()));
    }
}