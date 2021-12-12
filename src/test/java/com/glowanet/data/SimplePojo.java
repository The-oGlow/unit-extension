package com.glowanet.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SimplePojo {

    public static final int FIELD_COUNT  = 6;
    public static final int GETTER_COUNT = 5;
    public static final int SETTER_COUNT = 5;

    public static final int STATIC_COUNT = 8;
    public static final int FINAL_COUNT  = 8;
    public static final int CONST_COUNT  = 7;

    public static final Float CONST_FLOAT = 111f;
    public static       int   STATIC_INT  = 11;
    public final        int   FINAL_INT   = 12;

    private int          simpleInt;
    private char         onlyGetterChar;
    private byte         onlySetterByte;
    private String       simpleString;
    private Long[]       simpleLong;
    private List<String> simpleList = new ArrayList<>();

    public int getSimpleInt() {
        return simpleInt;
    }

    public void setSimpleInt(final int simpleInt) {
        this.simpleInt = simpleInt;
    }

    public char getOnlyGetterChar() {
        return onlyGetterChar;
    }

    public void setOnlySetterByte(final byte onlySetterByte) {
        this.onlySetterByte = onlySetterByte;
    }

    public String getSimpleString() {
        return simpleString;
    }

    public void setSimpleString(final String simpleString) {
        this.simpleString = simpleString;
    }

    public Long[] getSimpleLong() {
        return simpleLong;
    }

    public void setSimpleLong(final Long[] simpleLong) {
        this.simpleLong = simpleLong;
    }

    public List<String> getSimpleList() {
        return simpleList;
    }

    public void setSimpleList(final List<String> simpleList) {
        this.simpleList = simpleList;
    }

    @Override
    public String toString() {
        return "SimplePojo{" +
                "simpleInt=" + simpleInt +
                ", onlyGetterChar=" + onlyGetterChar +
                ", onlySetterByte=" + onlySetterByte +
                ", simpleString='" + simpleString + '\'' +
                ", simpleLong=" + Arrays.toString(simpleLong) +
                ", simpleList=" + simpleList +
                '}';
    }
}
