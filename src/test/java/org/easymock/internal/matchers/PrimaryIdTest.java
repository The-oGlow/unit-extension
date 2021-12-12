package org.easymock.internal.matchers;

import com.glowanet.data.SimplePojo;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import java.util.regex.Pattern;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.notNullValue;

public class PrimaryIdTest {

    protected SimplePojo expectedInstance;
    protected String     expectedInstanceFieldName  = "simpleInt";
    protected int        expectedInstanceFieldValue = 100;

    protected SimplePojo actualInstanceSame;
    protected SimplePojo actualInstanceDifferent;
    protected String     actualInstanceFieldName           = "simpleInt";
    protected int        actualInstanceFieldValueSame      = expectedInstanceFieldValue;
    protected int        actualInstanceFieldValueDifferent = actualInstanceFieldValueSame + 1;

    protected PrimaryId<SimplePojo> o2T;

    @Before
    public void setUp() throws Exception {
        expectedInstance = new SimplePojo();
        expectedInstance.setSimpleInt(expectedInstanceFieldValue);

        actualInstanceSame = new SimplePojo();
        actualInstanceSame.setSimpleInt(actualInstanceFieldValueSame);
        actualInstanceDifferent = new SimplePojo();
        actualInstanceDifferent.setSimpleInt(actualInstanceFieldValueDifferent);

        o2T = new PrimaryId<>(expectedInstance, expectedInstanceFieldName);
    }

    @Test
    public void testMatches_correctValue_return_true() {
        boolean expected = true;

        boolean actual = o2T.matches(actualInstanceSame);

        assertThat(actual, Matchers.equalTo(expected));
    }

    @Test
    public void testMatches_incorrectValue_return_false() {
        boolean expected = false;

        boolean actual = o2T.matches(actualInstanceDifferent);

        assertThat(actual, Matchers.equalTo(expected));
    }

    @Test
    public void testMatches_bothNull_return_false() {
        boolean expected = false;

        o2T = new PrimaryId<>(null, null);

        boolean actual = o2T.matches(null);

        assertThat(actual, Matchers.equalTo(expected));
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

    @Test
    public void testToString_return_values() {
        Pattern pattern = Pattern.compile("^" + o2T.getClass().getSimpleName() + "." //
                + "expectClazz=.+" + expectedInstance.getClass().getName() + ", " //
                + "expectedField=.+" + expectedInstanceFieldName + ",.+" //
                + "expectedValue=" + expectedInstanceFieldValue //
                + ".+", Pattern.CASE_INSENSITIVE);

        assertThat(o2T, notNullValue());
        assertThat(o2T.toString(), Matchers.matchesPattern(pattern));
    }

}