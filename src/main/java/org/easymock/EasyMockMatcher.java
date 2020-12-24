package org.easymock;

/**
 * Additional EasyMockMatcher class. Contains a list of additional matchers.
 *
 * @author Oliver Glowa
 * @see EasyMock
 * @since 0.10.000
 */
public class EasyMockMatcher {

    /**
     * @param expectedInstance an instance of the expected type
     * @param primIdFieldName  the field name which is used as primary-id.
     * @param <T>              type of the method argument to match
     *
     * @return {@code null}.
     */
    @SuppressWarnings("rawtypes")
    public static <T> T eqPrimaryId(final T expectedInstance, final String primIdFieldName) {
        //        EasyMock.reportMatcher(new PrimaryId(expectedInstance, primIdFieldName));
        return null;
    }
}
