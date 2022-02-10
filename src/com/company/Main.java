package com.company;

import java.lang.reflect.Array;
import java.time.Year;
import java.util.Objects;
import java.util.concurrent.TransferQueue;

public class Main {
  public static void main(String[] args) {
    ArrayCircularBoundedQueue<String> e = new ArrayCircularBoundedQueue<>(3);

  }
}


class ArrayCircularBoundedQueue<T> implements ICircularBoundedQueue<T> {

  private int capacity;
  private int size;

  private int front = 0;
  private int rear = 0;

  public ArrayCircularBoundedQueue (int capacity) {
    this.capacity = capacity;
  }

  @Override
  public void offer(T value) {

    if (rear == front - 1) front = (front + 1) % capacity;

    rear = (rear + 1) % capacity;
  }

  @Override
  public T poll() {


    if (front == rear) rear = (rear + 1) % capacity;

    front = (front + 1) % capacity;

    return null;
  }

  @Override
  public T peek() {
    return null;
  }

  @Override
  public void flush() {
    while (front != rear) {

      front += 1;
    }
  }

  @Override
  public boolean isEmpty() {
    return front == rear;
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


class MyDoublyLinkedList<T> implements DoublyLinkedList<T> {
  private final int capacity;
  private int size = 0;

  private Node<T> head;
  private Node<T> tail;

  public MyDoublyLinkedList(int capacity) {
    this.capacity = capacity;
  }

  static class Node<T> {
    public T value;
    public Node<T> previous;
    public Node<T> next;

    public Node(T value) {
      this.value = value;
    }
  }

  @Override
  public int size() {
    return size;
  }

  @Override
  public boolean isEmpty() {
    return head == null;
  }

  @Override
  public T get(int index) {
    Node<T> node = head;

    for(int currentIndex = 1; currentIndex <= index; currentIndex++) {
      node = node.next;
    }

    return node.value;
  }

  @Override
  public void set(int index, T value) {
    Node<T> currentNode = head;

    for(int currentIndex = 1; currentIndex <= index; currentIndex++) {
      currentNode = currentNode.next;
    }

    currentNode.value = value;
  }

  @Override
  public void add(int index, T value) {
    Node<T> previousNode;
    Node<T> currentNode = head;
    Node<T> newNode = new Node<>(value);

    size += 1;

    for(int currentIndex = 1; currentIndex <= index; currentIndex++) {
      currentNode = currentNode.next;
    }

    if (currentNode == head) {
      currentNode.previous = newNode;
      newNode.next = currentNode;

      head = newNode;

      return;
    }

    previousNode = currentNode.previous;

    previousNode.next = newNode;
    currentNode.previous = newNode;

    newNode.next = currentNode;
    newNode.previous = previousNode;

  }

  @Override
  public void remove(int index) {
    Node<T> nextNode;
    Node<T> previousNode;
    Node<T> currentNode = head;

    size -= 1;

    for(int currentIndex = 1; currentIndex <= index; currentIndex++) {
      currentNode = currentNode.next;
    }

    if (currentNode == tail) {
      previousNode = currentNode.previous;
      previousNode.next = null;

      tail = previousNode;

      return;
    }

    if (currentNode == head) {
      nextNode = currentNode.next;
      nextNode.previous = null;

      head = nextNode;

      return;
    }

    nextNode = currentNode.next;
    previousNode = currentNode.previous;

    nextNode.previous = previousNode;
    previousNode.next = nextNode;
  }

  @Override
  public void addFirst(T value) {
    if (isEmpty()) {
      init(value);
      return;
    }

    Node<T> newNode = new Node<>(value);

    size += 1;

    if (size > capacity) {
      tail.previous.next = null;
      tail = tail.previous;
    }

    head.previous = newNode;
    newNode.next = head;
    head = newNode;
  }

  @Override
  public void addLast(T value) {
    if (isEmpty()) {
      init(value);
      return;
    }

    Node<T> newNode = new Node<>(value);

    size += 1;

    if (size > capacity) {
      head.next.previous = null;
      head = head.next;

      size = capacity;
    }

    tail.next = newNode;
    newNode.previous = tail;
    tail = newNode;
  }

  @Override
  public void removeFirst() {
    remove(0);
  }

  @Override
  public void removeLast() {
    Node<T> previous = tail.previous;

    size -= 1;

    previous.next = null;
    tail = previous;
  }

  @Override
  public String toString() {
    if (isEmpty()) return "{}";

    Node<T> currentNode = head;

    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append('{');

    while (currentNode != null) {
      stringBuilder.append(currentNode.value.toString()).append(",");
      currentNode = currentNode.next;
    }

    stringBuilder.deleteCharAt(stringBuilder.length() - 1);

    stringBuilder.append('}');

    return stringBuilder.toString();
  }

  private void init(T value) {
    Node<T> newNode = new Node<>(value);

    size += 1;

    head = newNode;
    tail = newNode;
  }
}

interface DoublyLinkedList<T> {
  int size();

  boolean isEmpty();

  T get(int index);

  void set(int index, T value);

  void add(int index, T value);

  void remove(int index);

  void addFirst(T value);

  void addLast(T value);

  void removeFirst();

  void removeLast();
}
