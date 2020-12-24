package org.easymock;

import com.glowa_net.data.SimplePojo;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;

public class EasyMockMatcherTest {

    @Before
    public void setUp() {
    }

    @Test
    public void testEqPrimaryIdInitializationReturnsNull() {
        final SimplePojo expectedInstance = new SimplePojo();
        final String primIdFieldName = "simpleInt";

        final SimplePojo actual = EasyMockMatcher.eqPrimaryId(expectedInstance, primIdFieldName);
        assertThat(actual, nullValue());
    }
}
