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
public class PrimitiveConversionIT {

    @RunWith(Parameterized.class)
    public static class PrimitivePrimToObjIT {
        @Parameterized.Parameter
        public Class<?> valuePrim;
        @Parameterized.Parameter(1)
        public Class<?> expectedObj;

        @Parameterized.Parameters(name = "{index}: prim:{0} => obj:{1}")
        public static List<Object[]> data() {
            List<Object[]> testData = new ArrayList<>();
            for (Map.Entry<Class<?>, Class<?>> entry : Primitive.all().entrySet()) {
                testData.add(new Object[]{entry.getKey(), entry.getValue()});
            }
            testData.add(new Object[]{PrimitivePrimToObjIT.class, null});
            testData.add(new Object[]{null, null});
            return testData;
        }

        @Test
        public void testPrimToObj() {
            Class<?> actual = Primitive.primToObj(valuePrim);

            assertThat(actual, equalTo(expectedObj));
        }

        @Test
        public void testViceVersa() {
            Class<?> actual = Primitive.viceVersa(valuePrim);

            assertThat(actual, equalTo(expectedObj));
        }
    }

    @RunWith(Parameterized.class)
    public static class PrimitiveObjToPrimIT {
        @Parameterized.Parameter
        public Class<?> valueObj;
        @Parameterized.Parameter(1)
        public Class<?> expectedPrim;

        @Parameterized.Parameters(name = "{index}: prim:{0} => obj:{1}")
        public static List<Object[]> data() {
            List<Object[]> testData = new ArrayList<>();
            for (Map.Entry<Class<?>, Class<?>> entry : Primitive.all().entrySet()) {
                testData.add(new Object[]{entry.getValue(), entry.getKey()});
            }
            testData.add(new Object[]{PrimitiveObjToPrimIT.class, null});
            testData.add(new Object[]{null, null});
            return testData;
        }

        @Test
        public void testObjToPrim() {
            Class<?> actual = Primitive.objToPrim(valueObj);

            assertThat(actual, equalTo(expectedPrim));
        }

        @Test
        public void testViceVersa() {
            Class<?> actual = Primitive.viceVersa(valueObj);

            assertThat(actual, equalTo(expectedPrim));
        }
    }

    @RunWith(Parameterized.class)
    public static class PrimitiveIsPrimitiveIT {
        @Parameterized.Parameter
        public Class<?> primClazz;
        @Parameterized.Parameter(1)
        public Boolean  primExpect;

        @Parameterized.Parameters(name = "{index}: prim:{0} => expect:{1}")
        public static List<Object[]> data() {
            List<Object[]> testData = new ArrayList<>();
            for (Class<?> entry : Primitive.primitives()) {
                testData.add(new Object[]{entry, true});
            }
            testData.add(new Object[]{PrimitiveIsPrimitiveIT.class, false});
            testData.add(new Object[]{null, false});
            return testData;
        }

        @Test
        public void testIsPrimitive() {
            boolean actual = Primitive.isPrimitive(primClazz);

            assertThat(
                    String.format("Expected that clazz '%s' is %s primitive!", primClazz, primExpect ? "A" : "NOT"), actual, equalTo(primExpect));
        }
    }
}