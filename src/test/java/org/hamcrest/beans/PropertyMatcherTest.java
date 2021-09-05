package org.hamcrest.beans;

import com.glowa_net.data.SimplePojo;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.not;

public class PropertyMatcherTest {

    private static final SimplePojo DEFAULT_POJO;
    private static final SimplePojo DIFFERENT_POJO;

    private static final String PROP_NAME                = "simpleInt";
    private static final int    PROP_VALUE_DIFFERENT_INT = 123;
    private static final String PROP_VALUE_DIFFERENT_STR = "RELAX";

    private PropertyMatcher o2T;

    private BeanInfo           bean;
    private PropertyDescriptor descriptor;
    private Description        description;

    static {
        DEFAULT_POJO = new SimplePojo();
        DIFFERENT_POJO = new SimplePojo();
        DIFFERENT_POJO.setSimpleInt(PROP_VALUE_DIFFERENT_INT);
        DIFFERENT_POJO.setSimpleString(PROP_VALUE_DIFFERENT_STR);
    }

    @Before
    public void setUp() throws Exception {
        description = new StringDescription();
        bean = Introspector.getBeanInfo(DEFAULT_POJO.getClass());
        descriptor = Arrays.stream(bean.getPropertyDescriptors()).filter(pd -> pd.getName().matches(PROP_NAME)).findFirst().get();

        o2T = new PropertyMatcher(descriptor, DEFAULT_POJO);
    }

    @Test
    public void testMatches_correct_property_matches() {
        Object otherObject = DEFAULT_POJO;
        boolean expected1 = true;
        Matcher<String> expected2 = not(allOf(containsString(PROP_NAME), containsString(String.valueOf(PROP_VALUE_DIFFERENT_INT))));

        boolean actual1 = o2T.matches(otherObject, description);

        assertThat(actual1, equalTo(expected1));
        assertThat(description.toString(), expected2);
    }

    @Test
    public void testMatches_different_property_not_matches() {
        Object otherObject = DIFFERENT_POJO;
        boolean expected1 = false;
        Matcher<String> expected2 = allOf(containsString(PROP_NAME), containsString(String.valueOf(PROP_VALUE_DIFFERENT_INT)));

        boolean actual1 = o2T.matches(otherObject, description);

        assertThat(actual1, equalTo(expected1));
        assertThat(description.toString(), expected2);
    }

    @Test
    public void testDescribeTo_null_throw_npe() {
        Description testDescription = null;

        Throwable actual = Assert.assertThrows(Throwable.class, () -> o2T.describeTo(testDescription));

        assertThat(actual, instanceOf(NullPointerException.class));
    }

    @Test
    public void testDescribeTo_null_description_throw_npe() {
        Description testDescription = new Description.NullDescription();

        o2T.describeTo(testDescription);

        assertThat(testDescription.toString(), emptyString());
    }

    @Test
    public void testDescribeTo_empty_description_return_the_same() {
        Description testDescription = new StringDescription();

        o2T.describeTo(testDescription);

        assertThat(testDescription.toString(), containsString(String.valueOf(DEFAULT_POJO.getSimpleInt())));
    }
}