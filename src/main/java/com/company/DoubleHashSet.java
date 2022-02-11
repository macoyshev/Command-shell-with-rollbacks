package com.company;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Objects;

class DoubleHashSet<T> implements ISet<T> {
    private T[] hashTable;
    private final int capacity;
    private final int PRIME = 7;

    public DoubleHashSet(int capacity) {
        this.capacity = capacity;
        this.hashTable = (T[]) new Object[capacity];
    }

    @Override
    public void add(T item) {
    }

    @Override
    public void remove(T item) {

    }

    @Override
    public boolean contains(T item) {
        return false;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    public int getIndex(T item) {
        int index;

        for(int i = 0; i < capacity - 1; i++) {
            index = (hash1(item) + i * hash2(item)) % capacity;

            if (hashTable[index] == null)
                return index;
        }

        return -1;
    }

    public int hash1(T val) {
        int index = Math.abs(val.hashCode());
        return index % capacity;
    }

    public int hash2(T val) {
        int index = Math.abs(val.hashCode());
        return PRIME - index % PRIME;
    }
}
