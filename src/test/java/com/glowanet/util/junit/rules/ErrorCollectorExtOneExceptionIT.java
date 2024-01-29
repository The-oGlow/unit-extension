package com.glowanet.util.junit.rules;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ErrorCollectorExtOneExceptionIT {

    @Rule
    public ErrorCollectorExt collector = new ErrorCollectorExt();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testWithException() {
        collector.checkThat(20, equalTo(1));

        expectedException.expect(AssertionError.class);
    }

    @After
    public void tearDown() {
        assertThat(collector.getErrorSize(), equalTo(1));
    }

}