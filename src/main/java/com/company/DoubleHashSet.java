package com.company;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Objects;

public class DoubleHashSet<T> implements ISet<T> {
    private T[] hashTable;
    private final int capacity;
    private final int PRIME;
    private int size = 0;

    public DoubleHashSet(int capacity) {
        this.capacity = capacity;
        this.hashTable = (T[]) new Object[capacity];
        this.PRIME = getClosestPrime(capacity);
    }

    @Override
    public void add(T item) {
        int index = getIndex(item);

        if (index != -1 && hashTable[index] == null)
            hashTable[index] = item;

        size += 1;
    }

    @Override
    public void remove(T item) {
        int index = getIndex(item);

        if (index != -1)
            hashTable[index] = null;

        size -=1;
    }

    @Override
    public boolean contains(T item) {
        int index = getIndex(item);

        return index != -1 && hashTable[index] != null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        for(int i = 0; i < capacity; i++) {
            if (hashTable[i] != null) return false;
        }

        return true;
    }

    public int getIndex(T item) {
        int index;

        for(int i = 0; i < capacity; i++) {
            index = (hash1(item) + i * hash2(item)) % capacity;

            if (hashTable[index] == item) return index;
            if (hashTable[index] == null) return index;
        }

        return -1;
    }

    private int hash1(T val) {
        int index = Math.abs(val.hashCode());
        return index % capacity;
    }

    private int hash2(T val) {
        int index = Math.abs(val.hashCode());
        return PRIME - index % PRIME;
    }

    private int getClosestPrime(int number) {
        int prime = number - 1;
        while (!isPrime(prime)) prime--;

        return prime;
    }

    private boolean isPrime(int number) {
        if (number == 1)
            return false;

        if (number <= 3)
            return true;

        for(int i = 2; i < Math.sqrt(number); i++) {
            if (number % i == 0) return false;
        }

        return false;
    }
}
