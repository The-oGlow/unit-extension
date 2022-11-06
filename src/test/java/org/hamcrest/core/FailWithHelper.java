package org.hamcrest.core;

public class FailWithHelper {

    private static final FailWithHelper INSTANCE = new FailWithHelper();

    private FailWithHelper() {
        //nothing2do
    }

    public static FailWithHelper getInstance() {
        return INSTANCE;
    }

    public static class FailWithValidException extends Exception {
    }

    public static class FailWithInValidException extends Exception {
    }

    public void failWithValidException() throws Exception {
        throw new FailWithValidException();
    }

    public void failWithInValidException() throws Exception {
        throw new FailWithInValidException();
    }

    public void failWithNoException() throws Exception {
        //nothing2do
    }

}
