package com.company;

public class QueuedBoundedStack<T> implements IBoundedQueue<T> {
    private final DoublyLinkedCircularBoundedQueue<T> basicQueue;
    private final DoublyLinkedCircularBoundedQueue<T> helperQueue;

    public QueuedBoundedStack(int capacity) {
        basicQueue = new DoublyLinkedCircularBoundedQueue<>(capacity);
        helperQueue = new DoublyLinkedCircularBoundedQueue<>(capacity);
    }

    @Override
    public void push(T value) {
        if (basicQueue.isEmpty()) {
            basicQueue.offer(value);

        } else {
            int basisSize= basicQueue.size();

            for (int i = 0; i < basisSize; i++) {
                helperQueue.offer(basicQueue.poll());
            }

            basicQueue.offer(value);

            for (int i = 0; i < basisSize; i++) {
                if (basicQueue.isFull()) {
                    helperQueue.flush();
                    break;
                }
                basicQueue.offer(helperQueue.poll());
            }
        }
    }

    @Override
    public T pop() {
        return basicQueue.poll();
    }

    @Override
    public T top() {
        return basicQueue.peek();
    }

    @Override
    public void flush() {
        basicQueue.flush();
    }

    @Override
    public boolean isEmpty() {
        return basicQueue.isEmpty();
    }

    @Override
    public boolean isFull() {
        return basicQueue.isFull();
    }

    @Override
    public int size() {
        return basicQueue.size();
    }

    @Override
    public int capacity() {
        return basicQueue.capacity();
    }
}
