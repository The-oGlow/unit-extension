package org.hamcrest.beans;

import com.glowa_net.data.SimplePojo;
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Description;
import org.hamcrest.StringDescription;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThrows;

public class SamePropertiesValuesAsTest {

    protected static final String     DESCRIPTION_DEFAULT = "descriptionDefault";
    protected static final SimplePojo DEFAULT_POJO        = new SimplePojo();

    protected SamePropertiesValuesAs<SimplePojo> o2T;

    private static class SamePropertiesValuesAsTestClazz {
    }

    @Before
    public void setUp() {
        o2T = (SamePropertiesValuesAs<SimplePojo>) SamePropertiesValuesAs.samePropertiesValuesAs(DEFAULT_POJO);
    }

    public Description createDescription() {
        return new StringDescription().appendText(DESCRIPTION_DEFAULT.toString());
    }

    @Test
    public void testCreatesNewMatcher() {
        assertThat(o2T, notNullValue());
        assertThat(o2T, instanceOf(SamePropertiesValuesAs.class));
    }

    @Test
    public void testmatchesTheSameMatchesTrue() {
        final SimplePojo item = new SimplePojo();

        final boolean actual = o2T.matches(item);
        assertThat(actual, is(true));
    }

    @Test
    public void testmatchesOneDifferenceMatchesFalse() {
        final SimplePojo item = new SimplePojo();
        item.setSimpleInt(123);

        final boolean actual = o2T.matches(item);
        assertThat(actual, is(false));
    }

    @Test
    public void testmatchesMultipleDifferenceMatchesFalse() {
        final SimplePojo item = new SimplePojo();
        item.setSimpleInt(123);
        item.setSimpleString("RELAX");

        final boolean actual = o2T.matches(item);
        assertThat(actual, is(false));
    }

    @Test
    public void testmatchesNullMatchesFalse() {
        final boolean actual = o2T.matches(null);
        assertThat(actual, is(false));
    }

    @Test
    public void testOld() {
        final SimplePojo item = new SimplePojo();

        final SimplePojo actual = new SimplePojo();
        actual.setSimpleInt(2);
        actual.setSimpleString("111");
//        assertThat(actual, samePropertyValuesAs(item));
        assertThat(actual, SamePropertiesValuesAs.samePropertiesValuesAs(item));

    }

    @Test
    public void testDescribeMismatchModfiesDescription() {
        final SimplePojo item = new SimplePojo();
        item.setSimpleInt(123);
        item.setSimpleString("RELAX");
        final Description description = createDescription();

        o2T.describeMismatch(item, description);
        assertThat(description, not(equalTo(DESCRIPTION_DEFAULT)));
        assertThat(description.toString(), allOf( //
                containsString("simpleInt was <" + item.getSimpleInt() + ">"), //
                containsString("simpleString was \"" + item.getSimpleString() + "\"") //
        ));
    }

    @Test
    public void testDescribeMismatchNullValueReturnsNullValueDescription() {
        final Description description = createDescription();

        o2T.describeMismatch(null, description);
        assertThat(description, not(equalTo(DESCRIPTION_DEFAULT)));
        assertThat(description.toString(), containsString(DESCRIPTION_DEFAULT + "was null"));
    }

    public void testDescribeMismatchNullValueDescription() {
        final AssertionError actual = assertThrows(AssertionError.class, //
                () -> assertThrows(Throwable.class, //
                        () -> o2T.describeMismatch(DEFAULT_POJO, null)) //
        );
        assertThat(actual.getMessage(), containsString("but nothing was thrown"));
    }

    @Test
    public void testDescribeMismatchWrongTypeModfiesDescription() {
        final Description description = createDescription();

        final SamePropertiesValuesAsTestClazz actual = new SamePropertiesValuesAsTestClazz();
        o2T.describeMismatch(actual, description);
        assertThat(description, not(equalTo(DESCRIPTION_DEFAULT)));
        assertThat(description.toString(), allOf( //
                containsString("incompatible type"), //
                containsString(SamePropertiesValuesAsTestClazz.class.getSimpleName())) //
        );
    }

    @Test
    public void testDescribeToModifiesDescription() {
        final Description description = createDescription();

        o2T.describeTo(description);
        assertThat(description, not(equalTo(DESCRIPTION_DEFAULT)));
        assertThat(description.toString(), allOf( //
                containsString(DESCRIPTION_DEFAULT), //
                containsString(DEFAULT_POJO.getClass().getSimpleName() + " ["), //
                containsString("simpleInt: <" + DEFAULT_POJO.getSimpleInt() + ">") //
        ));
        assertThat(StringUtils.countMatches(description.toString(), ": null"), equalTo(2));
        assertThat(StringUtils.countMatches(description.toString(), ": []"), equalTo(1));
    }

    @Test(expected = NullPointerException.class)
    public void testDescribeToNullValueDescription() {
        o2T.describeTo(null);
    }
}
