package org.hamcrest;

import com.glowanet.util.hamcrest.AbstractPublicTest;
import org.hamcrest.core.FailWith;
import org.hamcrest.core.FailWithHelper;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.nullValue;

public class FailWithMatcherTest extends AbstractPublicTest {

    @Test
    public void failWith_withException_return_matcher() {
        Class<? extends Throwable> expectedException;
        actual = FailWithMatcher.failWith(FailWithHelper.FailWithValidException.class);
        assertThat(actual, instanceOf(FailWith.class));
        assertThat(((FailWith) actual).getExceptionClazz(), equalTo(FailWithHelper.FailWithValidException.class));
    }

    @Test
    public void failWith_withNull_return_matcher() {
        Class<? extends Throwable> expectedException;
        actual = FailWithMatcher.failWith(null);
        assertThat(actual, instanceOf(FailWith.class));
        assertThat(((FailWith) actual).getExceptionClazz(), nullValue());
    }

}