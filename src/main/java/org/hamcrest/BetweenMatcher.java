package org.hamcrest;

import org.apache.commons.lang3.tuple.Pair;
import org.hamcrest.core.IsBetween;
import org.hamcrest.core.IsBetweenWithBound;

/**
 * Contains matchers, which checks if a value is in a specific range.
 *
 * @author Oliver Glowa
 * @since 0.02.000
 */
public class BetweenMatcher {

    private BetweenMatcher() {
    }

    /**
     * Creates a matcher for {@code T}s that matches, if the value is between a given range.
     * <p>
     * Range start and end <strong>NOT</strong>included.
     *
     * @param from start value for the range
     * @param to   end value for the range
     * @param <T>  type of the values
     *
     * @return newly created matcher
     */
    public static <T extends Comparable<T>> Matcher<T> between(final T from, final T to) {
        return IsBetween.between(from, to);
    }

    /**
     * Creates a matcher for {@code T}s that matches, if the value is between a given range.
     * <p>
     * Range start and end <strong>NOT</strong>included.
     *
     * @param fromTo a pair with the start and end value for the range
     * @param <T>    type of the values
     *
     * @return newly created matcher
     */
    public static <T extends Comparable<T>> Matcher<T> between(final Pair<T, T> fromTo) {
        return IsBetween.between(fromTo.getLeft(), fromTo.getRight());
    }

    /**
     * Creates a matcher for {@code T}s that matches, if the value is between a given range.
     * <p>
     * Range start and end <strong>IS</strong>included.
     *
     * @param from start value for the range
     * @param to   end value for the range
     * @param <T>  type of the values
     *
     * @return newly created matcher
     */
    public static <T extends Comparable<T>> Matcher<T> betweenWithBound(final T from, final T to) {
        return IsBetweenWithBound.between(from, to);
    }

    /**
     * Creates a matcher for {@code T}s that matches, if the value is between a given range.
     * <p>
     * Range start and end <strong>IS</strong>included.
     *
     * @param fromTo a pair with the start and end value for the range
     * @param <T>    type of the values
     *
     * @return newly created matcher
     */
    public static <T extends Comparable<T>> Matcher<T> betweenWithBound(final Pair<T, T> fromTo) {
        return IsBetweenWithBound.between(fromTo.getLeft(), fromTo.getRight());
    }

}
