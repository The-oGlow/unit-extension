package org.hamcrest.beans;

import com.glowanet.data.SimplePojo;
import org.hamcrest.AbstractExtendedMatcherTest;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

import java.util.regex.Pattern;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;

public class HasSameValuesTest<T extends SimplePojo> extends AbstractExtendedMatcherTest<T> {

    protected static final Pattern EMPTY_FIELDS = Pattern.compile(DESCRIPTION_DEFAULT + "\\s*?\\{\\s+\\}", Pattern.MULTILINE + Pattern.DOTALL);

    protected static final String FIELD_SIMPLE_INT    = "simpleInt";
    protected static final String FIELD_SIMPLE_STRING = "simpleString";

    protected static final int    DEFAULT_INT      = 123;
    protected static final String DEFAULT_STRING   = "RELAX";
    protected static final int    DIFFERENT_INT    = 789;
    protected static final String DIFFERENT_STRING = "DON'T DO IT";

    @Override
    protected Matcher<T> createMatcher() {
        return HasSameValues.hasSameValues(prepareArgumentInMatcher());
    }

    @Override
    protected T prepareArgumentInMatcher() {
        T item = (T) new SimplePojo();
        item.setSimpleInt(DEFAULT_INT);
        item.setSimpleString(DEFAULT_STRING);
        return item;
    }

    @Override
    protected T prepareArgumentToCompareWith() {
        T item = (T) new SimplePojo();
        item.setSimpleInt(DIFFERENT_INT);
        item.setSimpleString(DIFFERENT_STRING);
        return item;
    }

    /* Section for {@link org.hamcrest.BaseMatcher} unit tests */

    @Override
    protected Matcher<String> prepareMatcher_objectsAreDifferent_check() {
        return allOf( //
                containsString(String.format("%s= expected: <%s> but: was <%s>", FIELD_SIMPLE_INT, DEFAULT_INT, DIFFERENT_INT)), //
                containsString(String.format("%s= expected: \"%s\" but: was \"%s\"", FIELD_SIMPLE_STRING, DEFAULT_STRING, DIFFERENT_STRING))//
        );
    }

    @Override
    protected Matcher<String> prepareMatcherDescriptionText_defaultDescription_check() {
        return containsString(HasSameValues.SAME_CONTENT);
    }

    @Override
    protected Matcher<String> prepareDescriptionText_missmatchContent_check() {
        return containsString(DESCRIPTION_DEFAULT);
    }

    @Override
    protected Matcher<String> prepareDescriptionText_missmatchType_check() {
        return containsString(DIFFERENT_CLAZZ_NAME);
    }

    /* Section for {@link org.hamcrest.TypeSafeMatcher} unit tests */

    @Override
    protected Matcher<String> prepareMatcherDescriptionText_missmatchSafely_sameObject_check() {
        return Matchers.matchesRegex(EMPTY_FIELDS);
    }

    @Override
    protected Matcher<String> prepareMatcherDescriptionText_missmatchSafely_differentObject_check() {
        return Matchers.matchesRegex(EMPTY_FIELDS);
    }

    @Override
    protected Matcher<String> prepareMatcherDescriptionText_missmatchSafely_nullObject_check() {
        return containsString(DESCRIPTION_DEFAULT + FIELD_NULL);
    }
}
