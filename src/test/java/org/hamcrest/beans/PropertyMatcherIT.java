package org.hamcrest.beans;

import org.hamcrest.Description;
import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.instanceOf;

public class PropertyMatcherIT extends PropertyMatcherTest {

    @Test
    public void testDescribeTo_withNull_throw_NPE() {
        Description testDescription = null;

        Throwable actual = Assert.assertThrows(Throwable.class, () -> o2T.describeTo(testDescription));

        assertThat(actual, instanceOf(NullPointerException.class));
    }

    @Test
    public void testDescribeTo_withNullDescription_return_empty() {
        Description testDescription = new Description.NullDescription();

        o2T.describeTo(testDescription);

        assertThat(testDescription.toString(), emptyString());
    }
}
