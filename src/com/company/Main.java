package com.company;

public class Main {
  public static void main(String[] args) {

  }
}


class DoublyLinkedCircularBoundedQueue<T> implements ICircularBoundedQueue<T> {
  private final MyDoublyLinkedList<T> deque;

  public DoublyLinkedCircularBoundedQueue(int capacity) {
    this.deque = new MyDoublyLinkedList<>(capacity);
  }

  @Override
  public void offer(T value) {
    deque.addLast(value);
  }

  @Override
  public T poll() {
    T front = deque.get(0);
    deque.removeFirst();

    return front;
  }

  @Override
  public T peek() {
    return deque.get(0);
  }

  @Override
  public void flush() {
    while (!deque.isEmpty())
      deque.removeLast();
  }

  @Override
  public boolean isEmpty() {
    return deque.isEmpty();
  }

  @Override
  public boolean isFull() {
    return deque.size() == deque.capacity();
  }

  @Override
  public int size() {
    return deque.size();
  }

  @Override
  public int capacity() {
    return deque.capacity();
  }

  @Override
  public String toString() {
    return deque.toString();
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


class QueuedBoundedStack<T> implements IBoundedStack<T> {
  private MyDoublyLinkedList<T> deque;

  public QueuedBoundedStack(int capacity) {
    this.deque = new MyDoublyLinkedList<>(capacity);
  }

  @Override
  public void push(T value) {
    deque.addLast(value);
  }

  @Override
  public T pop() {
    T rear = deque.getLast();
    deque.removeLast();
    return rear;
  }

  @Override
  public T top() {
    return deque.getLast();
  }

  @Override
  public void flush() {
    while (!deque.isEmpty()) deque.removeLast();
  }

  @Override
  public boolean isEmpty() {
    return deque.isEmpty();
  }

  @Override
  public boolean isFull() {
    return deque.capacity() == deque.size();
  }

  @Override
  public int size() {
    return deque.size();
  }

  @Override
  public int capacity() {
    return deque.capacity();
  }

  @Override
  public String toString() {
    return deque.toString();
  }
}

interface IBoundedStack<T> {
  void push(T value); // push an element onto the stack
                      // remove the oldest element
                      // when if stack is full

  T pop(); // remove an element from the top of the stack

  T top(); // look at the element at the top of the stack
           // (without removing it)

  void flush(); // remove all elements from the stack

  boolean isEmpty(); // is the stack empty?

  boolean isFull(); // is the stack full?

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
  public T getLast() {
    return tail.value;
  }

  @Override
  public T getFirst() {
    return head.value;
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

    } else {
      previousNode = currentNode.previous;

      previousNode.next = newNode;
      currentNode.previous = newNode;

      newNode.next = currentNode;
      newNode.previous = previousNode;
    }

    if (size > capacity) {
      tail.previous.next = null;
      tail = tail.previous;
    }
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

    if (tail == head) {
      tail = head = null;
    } else {
      previous.next = null;
      tail = previous;
    }
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

  public int capacity() {
    return capacity;
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


interface ISet<T> {
  void add(T item);// add item in the set

  void remove(T item);// remove an item from a set

  boolean contains(T item);// check if a item belongs to a set

  int size();// number of elements in a set

  boolean isEmpty();// check if the set is empty
}
