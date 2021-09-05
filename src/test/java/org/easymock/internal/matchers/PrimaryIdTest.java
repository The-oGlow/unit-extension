package org.easymock.internal.matchers;

import com.glowa_net.data.SimplePojo;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import java.util.regex.Pattern;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.notNullValue;

public class PrimaryIdTest {

    private SimplePojo expectedInstance;
    private String     expectedInstanceFieldName  = "simpleInt";
    private int        expectedInstanceFieldValue = 100;

    private PrimaryId<SimplePojo> o2T;

    @Before
    public void setUp() throws Exception {
        expectedInstance = new SimplePojo();
        expectedInstance.setSimpleInt(expectedInstanceFieldValue);

        o2T = new PrimaryId<>(expectedInstance, expectedInstanceFieldName);
    }

    @Test
    public void testCreateObject_withValues_everyFieldIsFilled() {
        assertThat(o2T, notNullValue());
        assertThat(o2T.toString(), containsString(String.valueOf(expectedInstance)));
        assertThat(o2T.toString(), containsString(expectedInstance.getClass().getName()));
        assertThat(o2T.toString(), containsString(expectedInstanceFieldName));
        assertThat(o2T.toString(), containsString(String.valueOf(expectedInstanceFieldValue)));
    }

    @Test
    public void testCreateObject_withNull_everythingIsNull() {
        Pattern pattern = Pattern.compile(".+\\w=null,.+\\w=null,.+\\w=null,.+\\w=null.+", Pattern.CASE_INSENSITIVE);

        o2T = new PrimaryId<>(null, null);

        assertThat(o2T, notNullValue());
        assertThat(o2T.toString(), Matchers.matchesPattern(pattern));
    }

    @Test
    public void matches() {
    }

    @Test
    public void testAppendTo_withValues() {
        StringBuffer actual = new StringBuffer();

        o2T.appendTo(actual);

        assertThat(actual, notNullValue());
        assertThat(actual.toString(), containsString(expectedInstance.getClass().getName()));
        assertThat(actual.toString(), containsString(expectedInstanceFieldName));
        assertThat(actual.toString(), containsString(String.valueOf(expectedInstanceFieldValue)));
    }

    @Test
    public void testAppendTo_withNull() {
        Pattern pattern = Pattern.compile("null with null=<null>", Pattern.CASE_INSENSITIVE);
        StringBuffer actual = new StringBuffer();

        o2T = new PrimaryId<>(null, null);
        o2T.appendTo(actual);

        assertThat(actual, notNullValue());
        assertThat(actual.toString(), Matchers.matchesPattern(pattern));
    }

}