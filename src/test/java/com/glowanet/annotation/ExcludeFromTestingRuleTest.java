package com.glowanet.annotation;

import com.glowanet.util.junit.rules.ExcludeFromTestingRule;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ExcludeFromTestingRuleTest {

    private static final Logger LOGGER = LogManager.getLogger();

    @Rule
    public ExcludeFromTestingRule excludeFromTestingRule = new ExcludeFromTestingRule();

    @ExcludeFromTesting
    @Test
    public void testIsIgnoredAlways() {
        fail("This test must be ignored always!");
    }

    @ExcludeFromTesting(assumeTrue = "assumeIsTrue")
    @Test
    public void testIsIgnored() {
        fail("This test must be ignored conditionally!");
    }

    public static boolean assumeIsTrue() {
        return true;
    }

    @SuppressWarnings({"java:S2698", "java:S2701"})
    @Test
    public void testIsNotIgnored() {
        LOGGER.info("This test must not be executed!");
        assertTrue(true);
    }
}
