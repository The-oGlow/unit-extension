package com.glowanet.util.junit.rules;

import com.glowanet.util.reflect.ReflectionHelper;
import org.junit.rules.ErrorCollector;

import java.util.ArrayList;
import java.util.List;

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

    protected List<Throwable> readErrors() {
        return ReflectionHelper.readField("errors", this);
    }

    protected void writeErrors(List<Throwable> errors) {
        ReflectionHelper.writeField("errors", this, errors);
    }

    public int getErrorSize() {
        return readErrors().size();
    }

    public void reset() {
        writeErrors(new ArrayList<Throwable>());
    }
}
