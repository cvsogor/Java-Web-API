package com.aditor.dashboard.spring.model;

/**
 * Created by dgordiichuk on 02.02.2016.
 */
public class MutableInteger {
    private int value;
    public MutableInteger(int value) {
        this.value = value;
    }
    public void set(int value) {
        this.value = value;
    }
    public void add(int value) {
        this.value+=value;
    }
    public int intValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
