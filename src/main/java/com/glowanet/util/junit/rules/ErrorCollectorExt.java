package com.glowanet.util.junit.rules;

import com.glowanet.util.reflect.ReflectionHelper;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * This is an {@code ErrorCollector} with some "EXTRA" features.
 * <ul>
 *     <li>get the number of errors in the collector</li>
 *     <li>reset the collector to zero</li>
 * </ul>
 *
 * @see ErrorCollector
 */
public class ErrorCollectorExt extends ErrorCollector {

    /**
     * @return List or collected throwables.
     */
    protected List<Throwable> readErrors() {
        return ReflectionHelper.readField("errors", this);
    }

    /**
     * Write a list of throwables into the collector.
     *
     * @param errors List of throwables
     */
    protected void writeErrors(List<Throwable> errors) {
        ReflectionHelper.writeField("errors", this, errors);
    }

    /**
     * @return How many errors are collected.
     */
    public int getErrorSize() {
        int size = 0;
        List<Throwable> errors = readErrors();
        if (errors != null) {
            size = readErrors().size();
        }
        return size;
    }

    @Test
    public void t() {
        Object a = null;

        int b = 2;

        System.out.println(Optional.ofNullable(a).orElse(b));

    }

    /**
     * @return List of collected error messages
     *
     * @see #getErrorTextsToString()
     */
    public List<String> getErrorTexts() {
        List<Throwable> errors = readErrors();
        List<String> errorTexts = new ArrayList<>();
        if (errors != null) {
            errorTexts = errors.stream()
                    .map(m -> Optional.ofNullable(m.getMessage()).orElse(m.getClass().getName()))
                    .collect(Collectors.toList());
        }
        return errorTexts;
    }

    /**
     * @return All collected error messages, delimited with '\n'
     *
     * @see #getErrorTexts()
     */
    public String getErrorTextsToString() {
        return getErrorTexts().stream()
                .map(String::toString)
                .collect(Collectors.joining("\n"));
    }

    /**
     * Clear the collector.
     */
    public void reset() {
        writeErrors(new ArrayList<Throwable>());
    }
}
