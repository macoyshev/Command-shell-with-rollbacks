package com.company;

import java.lang.reflect.Array;
import java.util.Objects;

public class Main {
  public static void main(String[] args) {
    ArrayCircularBoundedQueue<String> e = new ArrayCircularBoundedQueue<>(3);

  }
}


class ArrayCircularBoundedQueue<T> implements ICircularBoundedQueue<T> {

  private final T[] arr;

  private int capacity;
  private int size;

  private int front = 0;
  private int rear = 0;

  public ArrayCircularBoundedQueue (int capacity) {
    this.capacity = capacity;
    this.arr = (T[]) new Object[capacity];
  }

  @Override
  public void offer(T value) {
    arr[rear] = value;

    rear = (rear + 1) % capacity;
    if (rear == front) front = (front + 1) % capacity;
  }

  @Override
  public T poll() {
    T firstInQueue = arr[front];

    arr[front] = null;

    if (front == rear) rear = (rear + 1) % capacity;

    front = (front + 1) % capacity;

    return firstInQueue;
  }

  @Override
  public T peek() {
    return arr[front];
  }

  @Override
  public void flush() {
    while (front != rear) {
      arr[front] = null;

      front += 1;
    }
  }

  @Override
  public boolean isEmpty() {
    return front == rear && arr[front] == null;
  }

  @Override
  public boolean isFull() {
    return rear + 1 == front;
  }

  @Override
  public int size() {
    return capacity - 1 - Math.abs(front - rear);
  }

  @Override
  public int capacity() {
    return capacity;
  }
}


interface ICircularBoundedQueue<T> {
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
