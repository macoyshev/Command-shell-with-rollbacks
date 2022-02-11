package com.company;

public interface ISet<T> {
    void add(T item);// add item in the set

    void remove(T item);// remove an item from a set

    boolean contains(T item);// check if a item belongs to a set

    int size();// number of elements in a set

    boolean isEmpty();// check if the set is empty
}
