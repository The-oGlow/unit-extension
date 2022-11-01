package com.glowanet.reflect;

import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;

public class PrimitiveTest {

    private static final int NO_OF_PRIMITIVES_ALL = 14;
    private static final int NO_OF_PRIMITIVES     = NO_OF_PRIMITIVES_ALL / 2;

    @Test
    public void testTypesOfPrimitive() {
        List<Class<?>> actual = Primitive.typesOfPrimitive();

        assertThat(actual, hasSize(greaterThanOrEqualTo(NO_OF_PRIMITIVES)));
    }

    @Test
    public void testTypesOfAllPrimitive() {
        List<Class<?>> actual = Primitive.typesOfPrimitiveAll();

        assertThat(actual, hasSize(greaterThanOrEqualTo(NO_OF_PRIMITIVES_ALL)));
    }

    @Test
    public void testSize() {
        int actual = Primitive.size();

        assertThat(actual, greaterThanOrEqualTo(NO_OF_PRIMITIVES));
    }

    @Test
    public void testSizAll() {
        int actual = Primitive.sizeAll();

        assertThat(actual, greaterThanOrEqualTo(NO_OF_PRIMITIVES_ALL));
    }

    @Test
    public void testIsPrimitive() {
        boolean actual = Primitive.isPrimitive(int.class);

        assertThat(actual, equalTo(true));
    }

    @Test
    public void testIsPrimitiveWithNull() {
        boolean actual = Primitive.isPrimitive(null);

        assertThat(actual, equalTo(false));
    }

    @Test
    public void testIsPrimitiveWithNotPrimitive() {
        boolean actual = Primitive.isPrimitive(PrimitiveTest.class);

        assertThat(actual, equalTo(false));
    }
}