package org.hamcrest;

import com.glowa_net.data.SimplePojo;
import org.hamcrest.beans.HasSameValues;
import org.hamcrest.beans.SamePropertiesValuesAs;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.notNullValue;

public class BeanValuesMatcherTest {

    private final SimplePojo pojo = new SimplePojo();

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void haseSameValues() {
        Matcher<SimplePojo> actual = BeanValuesMatcher.haseSameValues(pojo);
        verifyMatcher(actual, HasSameValues.class);
    }


    @Test
    public void samePropertyValuesAs() {
        String[] ignoredProperties = {};
        Matcher<SimplePojo> actual = BeanValuesMatcher.samePropertyValuesAs(pojo, ignoredProperties);
        verifyMatcher(actual, SamePropertiesValuesAs.class);
    }

    private void verifyMatcher(Matcher<?> actual, Class<?> expectedClazz) {
        assertThat(actual, notNullValue());
        assertThat(actual, instanceOf(expectedClazz));
    }
}