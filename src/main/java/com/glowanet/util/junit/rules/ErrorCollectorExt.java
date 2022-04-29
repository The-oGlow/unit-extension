package com.glowanet.util.junit.rules;

import com.glowanet.util.reflect.ReflectionHelper;
import org.junit.rules.ErrorCollector;

import java.util.ArrayList;
import java.util.List;
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

    protected List<Throwable> readErrors() {
        return ReflectionHelper.readField("errors", this);
    }

    protected void writeErrors(List<Throwable> errors) {
        ReflectionHelper.writeField("errors", this, errors);
    }

    public int getErrorSize() {
        return readErrors().size();
    }

    public List<String> getErrorTexts() {
        List<Throwable> errors = readErrors();
        List<String> errorTexts = new ArrayList<>();
        if (errors != null) {
            errorTexts = errors.stream().map(Throwable::getMessage).collect(Collectors.toList());
        }
        return errorTexts;
    }

    public String getErrorTextsToString() {
        return getErrorTexts().stream().map(String::toString).collect(Collectors.joining("\n"));
    }

    public void reset() {
        writeErrors(new ArrayList<Throwable>());
    }
}
