package com.company;

public class DoublyLinkedCircularBoundedQueue<T> implements ICircularBoundedQueue<T> {
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
        if (isEmpty()) return null;

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
