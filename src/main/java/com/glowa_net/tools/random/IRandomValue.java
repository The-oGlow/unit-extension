package com.glowa_net.tools.random;

public interface IRandomValue<T> {

    T randomValue();

    T randomValue(T rangeStart, T rangeEnd);

}
