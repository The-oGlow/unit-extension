package com.glowanet.reflect;

import java.util.List;
import java.util.Map;

/**
 * Representing all primitive types and their corrosponding object type.
 */
@SuppressWarnings("java:S6411")
public class Primitive {

    /**
     * Assignment of primitive type to its object type
     *
     * @see #primitives()
     * @see #primToObj(Class)
     * @see #objToPrim(Class)
     */
    private static final Map<Class<?>, Class<?>> PRIM2OBJ = Map.of(
            Boolean.TYPE, Boolean.class,
            Integer.TYPE, Integer.class,
            Double.TYPE, Double.class,
            Float.TYPE, Float.class,
            Long.TYPE, Long.class,
            Character.TYPE, Character.class,
            Byte.TYPE, Byte.class
    );

    private Primitive() {
        //nothing2do
    }

    /**
     * @return list of all primitive java types only
     */
    public static List<Class<?>> primitives() {
        return List.copyOf(PRIM2OBJ.keySet());
    }

    /**
     * @return the amount of primitive types
     */
    public static int size() {
        return primitives().size();
    }

    /**
     * @return map of all primitive java types and their object type
     */
    public static Map<Class<?>, Class<?>> all() {
        return Map.copyOf(PRIM2OBJ);
    }

    /**
     * @return the amount of primitive types and their object types
     */
    public static int sizeAll() {
        return all().size();
    }

    /**
     * @param primClazz a java type
     *
     * @return true={@code primClazz} is a primitive type, else true
     */
    public static boolean isPrimitive(Class<?> primClazz) {
        if (primClazz == null) {
            return false;
        } else {
            return PRIM2OBJ.containsKey(primClazz);
        }
    }

    /**
     * @param primClazz a primitive type
     *
     * @return the corrosponding object type or null
     */
    public static Class<?> primToObj(Class<?> primClazz) {
        Class<?> result = null;
        if (primClazz != null) {
            result = PRIM2OBJ.get(primClazz);
        }
        return result;
    }

    /**
     * @param objClazz an object type
     *
     * @return the corrosponding primitive type or null
     */
    public static Class<?> objToPrim(Class<?> objClazz) {
        Class<?> result = null;
        if (objClazz != null) {
            for (Map.Entry<Class<?>, Class<?>> entry : PRIM2OBJ.entrySet()) {
                if (entry.getValue().equals(objClazz)) {
                    result = entry.getKey();
                }
            }
        }
        return result;
    }

    /**
     * @param objPrimClazz the primitive or object type
     *
     * @return the switched object or primitive type or null
     */
    public static Class<?> viceVersa(Class<?> objPrimClazz) {
        Class<?> result = null;
        if (objPrimClazz != null) {
            result = objToPrim(objPrimClazz);
            if (result == null) {
                result = primToObj(objPrimClazz);
            }
        }
        return result;
    }
}
