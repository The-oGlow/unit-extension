package com.glowanet.util.junit.rules;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ErrorCollectorNoExceptionIT {

    @Rule
    public ErrorCollectorExt collector = new ErrorCollectorExt();

    @Test
    public void testWithoutException() {
        collector.checkThat(10, equalTo(10));
    }

    @After
    public void tearDown() {
        assertThat(collector.getErrorSize(), equalTo(0));
    }
}