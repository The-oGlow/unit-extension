package com.glowanet.util.junit.rules;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;

public class ErrorCollectorExtIT {

    private ErrorCollectorExt o2t;
    private Throwable         firstError  = new NullPointerException("errMsg");
    private Throwable         secondError = new IllegalArgumentException();

    @Before
    public void setUp() {
        o2t = new ErrorCollectorExt();
        o2t.addError(firstError);
    }

    @Test
    public void readErrors_return_listWithOneElement() {
        List<Throwable> actual = o2t.readErrors();
        assertThat(actual, notNullValue());
        assertThat(actual, hasSize(1));
        assertThat(actual.toString(), containsString(firstError.getMessage()));
    }

    @Test
    public void writeErrors_change_content() {
        assertThat(o2t.getErrorSize(), equalTo(1));
        assertThat(o2t.readErrors(), hasItem(firstError));
        assertThat(o2t.readErrors().toString(), containsString(firstError.getMessage()));

        o2t.writeErrors(List.of(secondError));
        assertThat(o2t.getErrorSize(), equalTo(1));
        assertThat(o2t.readErrors(), hasItem(secondError));
        assertThat(o2t.readErrors().toString(), containsString(secondError.toString()));
    }

    @Test
    public void getErrorSize_return_one() {
        int actual = o2t.getErrorSize();
        assertThat(actual, equalTo(1));
    }

    @Test
    public void getErrorTexts_return_listWithOneElement() {
        List<String> actual = o2t.getErrorTexts();
        assertThat(actual, hasSize(1));
    }

    @Test
    public void getErrorTextsToString_return_oneErrorMessage() {
        String actual = o2t.getErrorTextsToString();
        assertThat(actual, not(emptyString()));
        assertThat(actual, containsString(firstError.getMessage()));
    }

    @Test
    public void reset_sets_to_zero() {
        assertThat(o2t.getErrorSize(), equalTo(1));
        o2t.reset();
        assertThat(o2t.getErrorSize(), equalTo(0));
    }
}