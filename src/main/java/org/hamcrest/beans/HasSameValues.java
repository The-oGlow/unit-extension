package org.hamcrest.beans;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.hamcrest.TypeSafeMatcher;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

/**
 * A matcher, which verifies, if two instances are totally equal.
 * In contrast to {@link SamePropertyValuesAs} this matcher reports every difference and not only the first one.
 *
 * @param <T> the type of the class which will be checked
 *
 * @author Oliver Glowa
 * @see org.hamcrest.beans.SamePropertyValuesAs
 * @see org.hamcrest.MatchersExtend
 * @since 0.10.000
 */
public class HasSameValues<T> extends TypeSafeMatcher<T> {

    static final         Description DESC_DESCRIPTION = new StringDescription();
    private static final String      INDENT           = "     ";

    private final T                                    expectedBean;
    private final Map<String, Object>                  fields         = new HashMap<>();
    private final List<Triple<String, Object, Object>> mismatchFields = new LinkedList<>();

    public static <T> HasSameValues<T> hasSameValues(final T expectedBean) {
        return new HasSameValues<>(expectedBean);
    }

    private HasSameValues(final T expectedBean) {
        super(expectedBean == null ? null : expectedBean.getClass());
        this.expectedBean = expectedBean;
        verifyInput(this.expectedBean);
        Field[] allFields = FieldUtils.getAllFields(this.expectedBean.getClass());
        for (Field singleField : allFields) {
            try {
                fields.put(singleField.getName(), FieldUtils.readField(singleField, this.expectedBean, true));
            } catch (IllegalAccessException e) {
                throw new IllegalArgumentException(e);
            }
        }
        initDescription();
    }

    private void verifyInput(final T expectedBean) {
        assertThat(expectedBean, notNullValue());
    }

    private void initDescription() {
        DESC_DESCRIPTION.appendText("The bean must have the total same content");
    }

    @Override
    protected boolean matchesSafely(T item) {
        assertThat("Actual item is 'null' and can't be compared!", item, notNullValue());

        for (Map.Entry<String, Object> expectedField : fields.entrySet()) {
            try {
                String key = expectedField.getKey();
                Object expectedValue = expectedField.getValue();
                Object actualValue = FieldUtils.readField(item, key, true);
                String description = checkThat("", actualValue, equalTo(expectedValue));
                if (description != null) {
                    mismatchFields.add(new ImmutableTriple<>(key, expectedValue, description));
                }
            } catch (IllegalAccessException e) {
                throw new IllegalArgumentException(e);
            }
        }
        return mismatchFields.isEmpty();
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(DESC_DESCRIPTION.toString());
    }

    @Override
    protected void describeMismatchSafely(T item, Description mismatchDescription) {
        if (item == null) {
            mismatchDescription.appendText("null");
        } else {
            StringBuilder mismatchText = new StringBuilder();
            mismatchText.append("{").append(System.lineSeparator()).append(INDENT);
            int i = 0;
            for (Triple<String, Object, Object> mismatchField : mismatchFields) {
                if (mismatchField.getRight() != null) {
                    if (i > 0) {
                        mismatchText.append(", ").append(System.lineSeparator()).append(INDENT);
                    }
                    mismatchText.append(mismatchField.getLeft()).append(":");
                    mismatchText.append(mismatchField.getRight());
                }
                i++;
            }
            mismatchText.append(System.lineSeparator()).append(INDENT).append("}");

            mismatchDescription.appendText(mismatchText.toString());
        }
    }

    private <F> String checkThat(String reason, F actual, Matcher<? super F> matcher) {
        if (!matcher.matches(actual)) {
            Description description = new StringDescription();
            description.appendText(reason).appendText(" expected: ").appendDescriptionOf(matcher).appendText(" but: ");
            matcher.describeMismatch(actual, description);
            return description.toString();
        }
        return null;
    }
}
