package com.company;

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
        if (isEmpty()) return null;

        Node<T> node = head;

        for (int currentIndex = 1; currentIndex <= index; currentIndex++) {
            node = node.next;
        }

        return node.value;
    }

    @Override
    public T getLast() {
        if (isEmpty()) return null;

        return tail.value;
    }

    @Override
    public T getFirst() {
        if (isEmpty()) return null;

        return head.value;
    }

    @Override
    public void set(int index, T value) {
        Node<T> currentNode = head;

        for (int currentIndex = 1; currentIndex <= index; currentIndex++) {
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

        for (int currentIndex = 1; currentIndex <= index; currentIndex++) {
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
        size -= 1;

        Node<T> nextNode;
        Node<T> previousNode;
        Node<T> currentNode = head;

        for (int currentIndex = 1; currentIndex <= index; currentIndex++) {
            currentNode = currentNode.next;
        }

        if (size == 0) {
            head = null;
            tail = null;

            return;
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
