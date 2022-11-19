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
 * In contrast to {@link org.hamcrest.beans.SamePropertyValuesAs} this matcher reports every difference and not only the first one.
 *
 * @param <T> the type of the clazz which will be checked
 *
 * @see org.hamcrest.MatchersExtend
 * @since 0.1.0
 */
public class HasSameValues<T> extends TypeSafeMatcher<T> {

    static final String INDENT                       = "     ";
    static final String SAME_CONTENT                 = "The bean must have the total same content";
    static final String ACTUAL_ITEM_CANT_BE_COMPARED = "Actual item is 'null' and can't be compared!";
    static final String DELIMITER                    = ", ";
    static final String LIST_START                   = "{";
    static final String LIST_END                     = "}";
    static final String LIST_EQU                     = "=";

    private final T                                    expectedBean;
    private final Map<String, Object>                  fields         = new HashMap<>();
    private final List<Triple<String, Object, Object>> mismatchFields = new LinkedList<>();

    private HasSameValues(final T expectedBean) {
        super(expectedBean == null ? null : expectedBean.getClass());
        this.expectedBean = expectedBean;
        verifyInput(this.expectedBean);
        Field[] allFields = FieldUtils.getAllFields(this.expectedBean.getClass()); //NOSONAR java:S2259
        for (Field singleField : allFields) {
            try {
                fields.put(singleField.getName(), FieldUtils.readField(singleField, this.expectedBean, true));
            } catch (IllegalAccessException e) {
                throw new IllegalArgumentException(e);
            }
        }
    }

    public static <T> HasSameValues<T> hasSameValues(final T expectedBean) {
        return new HasSameValues<>(expectedBean);
    }

    private void verifyInput(final T expectedBean) {
        assertThat(expectedBean, notNullValue());
    }

    @Override
    protected boolean matchesSafely(T item) {
        assertThat(ACTUAL_ITEM_CANT_BE_COMPARED, item, notNullValue());

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
        description.appendText(SAME_CONTENT);
    }

    @Override
    protected void describeMismatchSafely(T item, Description mismatchDescription) {
        if (item == null) {
            mismatchDescription.appendText("null");
        } else {
            StringBuilder mismatchText = new StringBuilder();
            mismatchText.append(LIST_START).append(System.lineSeparator()).append(INDENT);
            int i = 0;
            for (Triple<String, Object, Object> mismatchField : mismatchFields) {
                if (mismatchField.getRight() != null) {
                    if (i > 0) {
                        mismatchText.append(DELIMITER).append(System.lineSeparator()).append(INDENT);
                    }
                    mismatchText.append(mismatchField.getLeft()).append(LIST_EQU);
                    mismatchText.append(mismatchField.getRight());
                }
                i++;
            }
            mismatchText.append(System.lineSeparator()).append(INDENT).append(LIST_END);

            mismatchDescription.appendText(mismatchText.toString());
        }
    }

    private static <F> String checkThat(String reason, F actual, Matcher<? super F> matcher) {
        if (!matcher.matches(actual)) {
            Description description = new StringDescription();
            description.appendText(reason).appendText(" expected: ").appendDescriptionOf(matcher).appendText(" but: ");
            matcher.describeMismatch(actual, description);
            return description.toString();
        }
        return null;
    }
}
