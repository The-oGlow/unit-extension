package com.glowanet.tools.random;

import org.apache.commons.lang3.RandomUtils;

public class RandomValueBoolean extends AbstractRandomValue<Boolean> {

    RandomValueBoolean() {
        super(Boolean.class);
    }

    @Override
    public Boolean randomValue() {
        return RandomUtils.nextBoolean();
    }
}
