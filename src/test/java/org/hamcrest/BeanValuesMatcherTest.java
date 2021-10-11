package org.hamcrest;

import com.glowa_net.util.hamcrest.AbstractPublicTest;
import org.hamcrest.beans.HasSameValues;
import org.hamcrest.beans.SamePropertiesValuesAs;
import org.junit.Test;

/**
 * @see BeanValuesMatcher
 */
public class BeanValuesMatcherTest extends AbstractPublicTest {

    @Test
    public void testHasSameValues_return_aMatcher() {
        actual = BeanValuesMatcher.hasSameValues(pojo);
        verifyMatcher(HasSameValues.class);
    }

    @Test
    public void testSamePropertiesValuesAs_return_aMatcher() {
        String[] ignoredProperties = {};
        actual = BeanValuesMatcher.samePropertiesValuesAs(pojo, ignoredProperties);
        verifyMatcher(SamePropertiesValuesAs.class);
    }

}