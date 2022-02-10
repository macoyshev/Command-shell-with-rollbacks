package com.company;

interface DoublyLinkedList<T> {
    int size();

    boolean isEmpty();

    T get(int index);

    T getLast();

    T getFirst();

    void set(int index, T value);

    void add(int index, T value);

    void remove(int index);

    void addFirst(T value);

    void addLast(T value);

    void removeFirst();

    void removeLast();
}
