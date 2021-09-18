package org.easymock;

import com.glowa_net.data.SimplePojo;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;

public class EasyMockMatcherTest {

    @Test
    public void testEqPrimaryId_initialization_return_mull() {
        final SimplePojo expectedInstance = new SimplePojo();
        final String primIdFieldName = "simpleInt";

        final SimplePojo actual = EasyMockMatcher.eqPrimaryId(expectedInstance, primIdFieldName);
        assertThat(actual, nullValue());
    }
}
