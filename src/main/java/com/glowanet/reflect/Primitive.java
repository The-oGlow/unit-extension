package com.glowanet.reflect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Representing all primitive types and their corrosponding object type.
 */
public class Primitive {

    /**
     * Assignment of primitive type to its object type
     *
     * @see #typesOfPrimitive()
     * @see #primToObj(Class)
     * @see #objToPrim(Class)
     */
    protected static final Map<Class<?>, Class<?>> PRIM2OBJ = Map.of( //NOSONAR java:S6411
            Boolean.TYPE, Boolean.class,
            Integer.TYPE, Integer.class,
            Double.TYPE, Double.class,
            Float.TYPE, Float.class,
            Long.TYPE, Long.class,
            Character.TYPE, Character.class,
            Byte.TYPE, Byte.class
    );

    /**
     * List of all primitive types.
     *
     * @see #typesOfPrimitiveAll()
     */
    protected static final List<Class<?>> PRIM_ALL;

    static {
        PRIM_ALL = new ArrayList<>(PRIM2OBJ.keySet());
        PRIM_ALL.addAll(PRIM2OBJ.values());
    }

    private Primitive() {
        //nothing2do
    }

    /**
     * @return list of all primitive java types only
     */
    public static List<Class<?>> typesOfPrimitive() {
        return List.copyOf(PRIM2OBJ.keySet());
    }

    /**
     * @return the amount of primitive types
     */
    public static int size() {
        return typesOfPrimitive().size();
    }

    /**
     * @return list of all primitive java types and their object type
     */
    public static List<Class<?>> typesOfPrimitiveAll() {
        return Collections.unmodifiableList(PRIM_ALL);
    }

    /**
     * @return the amount of primitive types and their object types
     */
    public static int sizeAll() {
        return typesOfPrimitiveAll().size();
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
        if (primClazz == null) {
            return null;
        } else {
            return PRIM2OBJ.get(primClazz);
        }
    }

    /**
     * @param objClazz an object type
     *
     * @return the corrosponding primitive type or null
     */
    public static Class<?> objToPrim(Class<?> objClazz) {
        if (objClazz == null) {
            return null;
        } else {
            for (Map.Entry<Class<?>, Class<?>> entry : PRIM2OBJ.entrySet()) {
                if (entry.getValue().equals(objClazz)) {
                    return entry.getKey();
                }
            }
            return null;
        }
    }
}
