package com.glowanet.tools.random;

public abstract class AbstractRandomValue<T> implements IRandomValue<T> {

    private final Class<T> typeOfT;

    AbstractRandomValue(Class<T> typeOfT) {
        this.typeOfT = typeOfT;
    }

    public Class<T> getTypeOfT() {
        return typeOfT;
    }

    @Override
    public T randomValue(T rangeStart, T rangeEnd) {
        return randomValue();
    }
    
}
