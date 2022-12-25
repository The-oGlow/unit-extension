package org.hamcrest.beans;

import com.glowanet.util.junit.TestResultHelper;
import org.hamcrest.Description;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyString;

public class PropertyMatcherIT extends PropertyMatcherTest {

    @Test
    public void testDescribeTo_withNull_throw_NPE() {
        Description testDescription = null;
        TestResultHelper.verifyException(() -> o2T.describeTo(testDescription), NullPointerException.class);
    }

    @Test
    public void testDescribeTo_withNullDescription_return_empty() {
        Description testDescription = new Description.NullDescription();

        o2T.describeTo(testDescription);

        assertThat(testDescription.toString(), emptyString());
    }
}
