package org.easymock.internal.matchers;

import com.glowanet.data.SimplePojo;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.regex.Pattern;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.notNullValue;

public class PrimaryIdIT extends PrimaryIdTest {
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
    public void testMatches_nullValue_return_false() {
        boolean expected = false;

        boolean actual = o2T.matches(null);

        assertThat(actual, Matchers.equalTo(expected));
    }

    @Test
    public void testMatches_differentObject_return_false() {
        boolean expected = false;

        boolean actual = o2T.matches(Integer.valueOf(actualInstanceFieldValueSame));

        assertThat(actual, Matchers.equalTo(expected));
    }

    @Test
    public void testMatches_emptyObject_return_false() {
        boolean expected = false;

        boolean actual = o2T.matches(new SimplePojo());

        assertThat(actual, Matchers.equalTo(expected));
    }

}
