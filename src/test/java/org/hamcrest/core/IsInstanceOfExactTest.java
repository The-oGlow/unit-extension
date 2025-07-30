package org.hamcrest.core;

import com.glowanet.data.SimplePojo;
import com.glowanet.data.SimpleSerializable;
import com.glowanet.util.reflect.ReflectionHelper;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class IsInstanceOfExactTest {

    private Matcher<Object> o2T;

    @Parameterized.Parameters(name = "{index}: clazz:{0}, expected:{1}")
    public static List<Object[]> data() {
        return Arrays.asList(
                new Object[]{null, false},
                new Object[]{"string", false},
                new Object[]{new SimpleSerializable(), false},
                new Object[]{new SimplePojo(), true});
    }

    @Parameterized.Parameter
    public Object  testObject;
    @Parameterized.Parameter(1)
    public boolean expectedResult;

    @Before
    public void setUp() {
        o2T = IsInstanceOfExact.instanceOfExact(SimplePojo.class);
    }

    protected void verifyTestObject(Object o2T) {
        assertThat(o2T, notNullValue());
        assertThat(o2T.getClass(), equalTo(IsInstanceOfExact.class));
        assertThat(ReflectionHelper.readField("expectedClass", o2T), equalTo(SimplePojo.class));
        assertThat(ReflectionHelper.readField("matchableClass", o2T), equalTo(SimplePojo.class));
    }

    @Test
    public void testMatches() {
        boolean actual = o2T.matches(testObject);
        assertThat(actual, equalTo(expectedResult));
    }

    @Test
    public void instanceOfExact() {
        Matcher<Object> actual = IsInstanceOfExact.instanceOfExact(SimplePojo.class);
        verifyTestObject(actual);
    }

    @Test
    public void anyExact() {
        Matcher<SimplePojo> actual = IsInstanceOfExact.anyExact(SimplePojo.class);
        verifyTestObject(actual);
    }
}