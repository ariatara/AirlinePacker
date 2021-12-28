package com.example.airlinepacker;

import java.io.Serializable;

public class KeyValuePair<S, T> implements Serializable {

    public KeyValuePair() {

    }

    public KeyValuePair(S key, T value) {
        this.key = key;
        this.value = value;
    }

    public void set(S key, T value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String toString() {

        return "Weight - " + this.key + ", Value - " + this.value;
    }

    public S getKey() {
        return this.key;
    }

    public T getValue() {
        return this.value;
    }

    private S key;
    private T value;
}
