package com.glowanet.reflect;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@RunWith(Enclosed.class)
public class PrimitiveConversionTest {

    @RunWith(Parameterized.class)
    public static class PrimitivePrimToObjTest {
        @Parameterized.Parameter
        public Class<?> valuePrim;
        @Parameterized.Parameter(1)
        public Class<?> expectedObj;

        @Parameterized.Parameters(name = "{index}: prim:{0} => obj:{1}")
        public static List<Object[]> data() {
            List<Object[]> testData = new ArrayList<>();
            for (Map.Entry<Class<?>, Class<?>> entry : Primitive.PRIM2OBJ.entrySet()) {
                testData.add(new Object[]{entry.getKey(), entry.getValue()});
            }
            testData.add(new Object[]{PrimitiveConversionTest.class, null});
            testData.add(new Object[]{null, null});
            return testData;
        }

        @Test
        public void testPrimToObj() {
            Class<?> actual = Primitive.primToObj(valuePrim);

            assertThat(actual, equalTo(expectedObj));
        }
    }

    @RunWith(Parameterized.class)
    public static class PrimitiveObjToPrimTest {
        @Parameterized.Parameter
        public Class<?> valueObj;
        @Parameterized.Parameter(1)
        public Class<?> expectedPrim;

        @Parameterized.Parameters(name = "{index}: prim:{0} => obj:{1}")
        public static List<Object[]> data() {
            List<Object[]> testData = new ArrayList<>();
            for (Map.Entry<Class<?>, Class<?>> entry : Primitive.PRIM2OBJ.entrySet()) {
                testData.add(new Object[]{entry.getValue(), entry.getKey()});
            }
            testData.add(new Object[]{PrimitiveConversionTest.class, null});
            testData.add(new Object[]{null, null});
            return testData;
        }

        @Test
        public void testObjToPrim() {
            Class<?> actual = Primitive.objToPrim(valueObj);

            assertThat(actual, equalTo(expectedPrim));
        }
    }
}