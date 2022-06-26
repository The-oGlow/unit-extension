package org.hamcrest.beans;

import com.glowanet.data.SimplePojo;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThrows;

public class PropertyMatcherTest {

    protected static final SimplePojo DEFAULT_POJO;
    protected static final SimplePojo DIFFERENT_POJO;

    protected static final String PROP_NAME                = "simpleInt";
    protected static final int    PROP_VALUE_DIFFERENT_INT = 123;
    protected static final String PROP_VALUE_DIFFERENT_STR = "RELAX";

    private static final String PROP_NAME_SETTER = "onlySetterByte";

    protected PropertyMatcher o2T;

    protected PropertyDescriptor descriptor;
    protected Description        description;

    static {
        DEFAULT_POJO = new SimplePojo();
        DIFFERENT_POJO = new SimplePojo();
        DIFFERENT_POJO.setSimpleInt(PROP_VALUE_DIFFERENT_INT);
        DIFFERENT_POJO.setSimpleString(PROP_VALUE_DIFFERENT_STR);
    }

    @Before
    public void setUp() throws IntrospectionException {
        description = new StringDescription();
        descriptor = prepareDescriptor(DEFAULT_POJO, PROP_NAME);

        o2T = PropertyMatcher.matchProperty(descriptor, DEFAULT_POJO);
    }

    protected PropertyDescriptor prepareDescriptor(Object bean, String propertyName) throws IntrospectionException {
        BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
        return Arrays.stream(beanInfo.getPropertyDescriptors()).filter(pd -> pd.getName().matches(propertyName)).findFirst().get();
    }

    @Test
    public void testCreates_matcherCreated() {
        assertThat(o2T, notNullValue());
        assertThat(o2T, instanceOf(PropertyMatcher.class));
    }

    @Test
    public void testMatches_sameProperty_return_true() {
        Object otherObject = DEFAULT_POJO;
        boolean expectedMatch = true;
        Matcher<String> expectedDescription = not(allOf(containsString(PROP_NAME), containsString(String.valueOf(PROP_VALUE_DIFFERENT_INT))));

        boolean actualMatch = o2T.matches(otherObject, description);

        assertThat(actualMatch, equalTo(expectedMatch));
        assertThat(description.toString(), expectedDescription);
    }

    @Test
    public void testMatches_differentProperty_return_false() {
        Object otherObject = DIFFERENT_POJO;
        boolean expectedMatch = false;
        Matcher<String> expectedDescription = allOf(containsString(PROP_NAME), containsString(String.valueOf(PROP_VALUE_DIFFERENT_INT)));

        boolean actualMatch = o2T.matches(otherObject, description);

        assertThat(actualMatch, equalTo(expectedMatch));
        assertThat(description.toString(), expectedDescription);
    }

    @Test
    public void testmatches_noGetter_return_false() throws IntrospectionException {
        descriptor = prepareDescriptor(DEFAULT_POJO, PROP_NAME_SETTER);
        o2T = PropertyMatcher.matchProperty(descriptor, DEFAULT_POJO);
        Object otherObject = DEFAULT_POJO;

        boolean expectedMatch = false;
        Matcher<String> expectedDescription = equalTo(PROP_NAME_SETTER + " was null");
        boolean actualMatch = o2T.matches(otherObject, description);

        assertThat(actualMatch, equalTo(expectedMatch));
        assertThat(description.toString(), expectedDescription);
    }

    @Test
    public void testDescribeTo_withEmpty_return_the_same() {
        Description testDescription = new StringDescription();

        o2T.describeTo(testDescription);

        assertThat(testDescription.toString(), containsString(String.valueOf(DEFAULT_POJO.getSimpleInt())));
    }

    @Test
    public void testReadProperty_return_propertyValue() {
        Object target = DIFFERENT_POJO;
        Method method = (Method) ReflectionTestUtils.getField(o2T, "readMethod");

        Object actual = o2T.readProperty(method, target);

        assertThat(actual, equalTo(PROP_VALUE_DIFFERENT_INT));
    }

    @Test
    public void testReadProperty_noGetter_throw_IAE() throws IntrospectionException {
        Object target = DEFAULT_POJO;
        descriptor = prepareDescriptor(target, PROP_NAME_SETTER);
        o2T = PropertyMatcher.matchProperty(descriptor, DEFAULT_POJO);

        Method method = (Method) ReflectionTestUtils.getField(o2T, "readMethod");

        Throwable actual = assertThrows("Throwable raised!", Throwable.class, () -> o2T.readProperty(method, target));

        assertThat(actual, instanceOf(IllegalArgumentException.class));
    }

}