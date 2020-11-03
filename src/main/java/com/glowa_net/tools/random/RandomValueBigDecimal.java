package com.glowa_net.tools.random;

import java.math.BigDecimal;

import org.apache.commons.lang3.RandomUtils;

public class RandomValueBigDecimal extends AbstractRandomValue<BigDecimal> {

    RandomValueBigDecimal() {
        super(BigDecimal.class);
    }

    @Override
    public BigDecimal randomValue() {
        return BigDecimal.valueOf(RandomUtils.nextLong());
    }

    @Override
    public BigDecimal randomValue(BigDecimal rangeStart, BigDecimal rangeEnd) {
        return BigDecimal.valueOf(RandomUtils.nextLong(rangeStart.longValue(), rangeEnd.longValue()));
    }
}
