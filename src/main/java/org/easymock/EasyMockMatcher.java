package org.easymock;

import org.easymock.internal.matchers.PrimaryId;

/**
 * Additional EasyMockMatcher clazz. Contains a list of additional matchers.
 *
 * @see EasyMock
 * @since 0.1.0
 */
public class EasyMockMatcher {

    private EasyMockMatcher() {
        // hide public constructor
    }

    /**
     * @param expectedInstance an instance of the expected type
     * @param primIdFieldName  the field name which is used as primary-id.
     * @param <T>              type of the method argument to match
     *
     * @return {@code null}.
     */
    public static <T> T eqPrimaryId(final T expectedInstance, final String primIdFieldName) {
        EasyMock.reportMatcher(new PrimaryId<>(expectedInstance, primIdFieldName));
        return null;
    }
}
