package com.company;

public interface ICircularBoundedQueue<T> {
    void offer(T value);  // insert an element to the rear of the queue

    // overwrite the oldest elements
    // when the queue is full
    T poll(); // remove an element from the front of the queue

    T peek(); // look at the element at the front of the queue

    // (without removing it)
    void flush(); // remove all elements from the queue

    boolean isEmpty(); // is the queue empty?

    boolean isFull(); // is the queue full?

    int size(); // number of elements

    int capacity(); // maximum capacity
}
