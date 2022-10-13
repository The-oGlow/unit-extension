package com.glowanet.data;

import java.io.Serializable;
import java.util.Objects;

public class SimpleSerializable extends SimplePojo implements Serializable {

    private static final long serialVersionUID = -1L;

    private transient Float simpleFloat;

    public Float getSimpleFloat() {
        return simpleFloat;
    }

    public void setSimpleFloat(Float simpleFloat) {
        this.simpleFloat = simpleFloat;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimpleSerializable that = (SimpleSerializable) o;
        return getSimpleFloat().equals(that.getSimpleFloat());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSimpleFloat());
    }

    @Override
    public String toString() {
        return "SimpleSerializable{" +
                "simpleFloat=" + simpleFloat +
                "} " + super.toString();
    }
}
