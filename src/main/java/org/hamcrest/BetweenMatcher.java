package org.hamcrest;

import org.hamcrest.core.IsBetween;
import org.hamcrest.core.IsBetweenWithBound;

/**
 * Contains matchers, which checks if a value is in a specific range.
 *
 * @author Oliver Glowa
 * @since 0.02.000
 */
public class BetweenMatcher {

    /**
     * Singleton with only static methods has no public constructor.
     */
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
    public static <T extends Comparable<T>> Matcher<T> between(T from, T to) {
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
    public static <T extends Comparable<T>> Matcher<T> between(IsBetween.Range<T> fromTo) {
        return IsBetween.between(fromTo);
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
    public static <T extends Comparable<T>> Matcher<T> betweenWithBound(T from, T to) {
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
    public static <T extends Comparable<T>> Matcher<T> betweenWithBound(IsBetween.Range<T> fromTo) {
        return IsBetweenWithBound.between(fromTo);
    }

}
