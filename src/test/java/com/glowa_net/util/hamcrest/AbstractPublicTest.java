package com.glowa_net.util.hamcrest;

import com.glowa_net.data.SimplePojo;
import org.hamcrest.Matcher;
import org.hamcrest.core.IsBetween;
import org.junit.Before;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.notNullValue;

/*
 * Base class for testing all the classes which are used for public access the matcher.
 */
abstract public class AbstractPublicTest {

    protected static final String                   methodName               = "fooMethod";
    protected static final Class<?>                 annotationClazz          = AbstractPublicTest.class;
    protected static final String                   annotationParameterKey   = "fooParameter";
    protected static final Object                   annotationParameterValue = "fooValue";
    protected static final Object                   expectedBean             = new SimplePojo();
    protected static final Integer                  from                     = 10;
    protected static final Integer                  to                       = 100;
    protected static final IsBetween.Range<Integer> rangeFromTo              = new IsBetween.Range<>(from, to);

    protected SimplePojo pojo;
    protected Matcher<?> actual;

    @Before
    public void setUp() throws Exception {
        pojo = new SimplePojo();
    }

    /**
     * If the matcher works correct, is irrelevant for this test.
     *
     * @param expectedClazz the expected type of the matcher
     */
    protected void verifyMatcher(Class<?> expectedClazz) {
        verifyMatcher(actual, expectedClazz);
    }

    /*
     * If the matcher works correct, is irrelevant for this test.
     *
     * @param actual the current matcher
     * @param expectedClazz the expected type of the matcher
     */
    protected void verifyMatcher(Matcher<?> current, Class<?> expectedClazz) {
        assertThat(current, notNullValue());
        assertThat(current, instanceOf(expectedClazz));
    }
}
