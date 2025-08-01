package com.glowanet.reflect;

import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;

public class PrimitiveTest {

    private static final int NO_OF_PRIMITIVES_ALL = 14;
    private static final int NO_OF_PRIMITIVES     = NO_OF_PRIMITIVES_ALL / 2;

    @Test
    public void testPrimitive() {
        List<Class<?>> actual = Primitive.primitives();

        assertThat(actual, hasSize(greaterThanOrEqualTo(NO_OF_PRIMITIVES)));
    }

    @Test
    public void testPrimitiveAll() {
        List<Class<?>> actual = Primitive.all();

        assertThat(actual, hasSize(greaterThanOrEqualTo(NO_OF_PRIMITIVES_ALL)));
    }

    @Test
    public void testPrimitivesAllMap() {
        Map<Class<?>, Class<?>> actual = Primitive.allMap();

        assertThat(actual.entrySet(), hasSize(greaterThanOrEqualTo(NO_OF_PRIMITIVES)));
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
    public void testIsPrimitiveWithPrimitiveIsTrue() {
        boolean actual = Primitive.isPrimitive(int.class);

        assertThat(actual, equalTo(true));
    }

    @Test
    public void testIsPrimitiveWithObjectIsFalse() {
        boolean actual = Primitive.isPrimitive(Integer.class);

        assertThat(actual, equalTo(false));
    }

    @Test
    public void testIsPrimitiveWithPrimitiveBothDirectionIsTrue() {
        boolean actual = Primitive.isPrimitive(float.class, true);

        assertThat(actual, equalTo(true));
    }

    @Test
    public void testIsPrimitiveWithObjectBothDirectionIsTrue() {
        boolean actual = Primitive.isPrimitive(Float.class, true);

        assertThat(actual, equalTo(true));
    }

    @Test
    public void testPrimToObjInt2Integer() {
        Class<?> actual = Primitive.primToObj(int.class);

        assertThat(actual, equalTo(Integer.class));
    }

    @Test
    public void testObjToPrimInteger2Int() {
        Class<?> actual = Primitive.objToPrim(Integer.class);

        assertThat(actual, equalTo(int.class));
    }

    @Test
    public void testViceVersaInt2Integer() {
        Class<?> actual = Primitive.viceVersa(int.class);

        assertThat(actual, equalTo(Integer.class));
    }

    @Test
    public void testViceVersaInteger2Int() {
        Class<?> actual = Primitive.viceVersa(Integer.class);

        assertThat(actual, equalTo(int.class));
    }
}