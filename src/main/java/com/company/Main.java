//package com.company;

import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        CommandShellWithRollbacks.run();
    }
}

class CommandShellWithRollbacks {
    private static DoublyLinkedCircularBoundedQueue<String> commands;

    public static void run() {
        Scanner scanner = new Scanner(System.in);

        int commandsCount = scanner.nextInt();
        int backUpDepth = scanner.nextInt();
        scanner.nextLine();

        FileSystem.init(commandsCount, backUpDepth);

        for (int i = 0; i < commandsCount; i++) {
            String[] data = scanner.nextLine().split(" ");

            try {
                switch (data[0]) {
                    case "NEW":
                        FileSystem.create(data[1]);

                        break;
                    case "REMOVE":
                        FileSystem.remove(data[1]);

                        break;
                    case "LIST":
                        FileSystem.displayAllFilesAndDirs();

                        break;
                    case "UNDO":
                        int countBackUps = 1;
                        if (data.length == 2) countBackUps = Integer.parseInt(data[1]);

                        if (countBackUps> FileSystem.countOfBackups())
                            if (countBackUps == 1)
                                throw new RuntimeException("UNDO");
                            else throw new RuntimeException("UNDO " + countBackUps);

                        for(int j = 0; j < countBackUps; j++) {
                            FileSystem.makeBackUp();
                        }

                        break;
                }
            } catch (RuntimeException e) {
                System.out.println("ERROR: cannot execute " + e.getMessage());
            }
        }
    }
}

class CommandShellWithoutRollbacks {
    private static DoublyLinkedCircularBoundedQueue<String> commands;

    public static void run() {
        Scanner scanner = new Scanner(System.in);

        int commandsCount = Integer.parseInt(scanner.nextLine());

        FileSystem.init(commandsCount);

        for(int i = 0; i < commandsCount; i++) {
            String[] data = scanner.nextLine().split(" ");

            try {
                switch (data[0]) {
                    case "NEW":
                        FileSystem.create(data[1]);

                        break;
                    case "REMOVE":
                        FileSystem.remove(data[1]);

                        break;
                    case "LIST":
                        FileSystem.displayAllFilesAndDirs();

                        break;
                }
            } catch (RuntimeException e) {
                System.out.println("ERROR: cannot execute " + e.getMessage());
            }
        }
    }
}

class BoundedCommandsQueue {
    public static void main(String[] args) {
        run();
    }

    private final ICircularBoundedQueue<String> commands;
    private final int capacity;

    public BoundedCommandsQueue(int capacity) {
        this.capacity = capacity;
        commands = new DoublyLinkedCircularBoundedQueue<>(capacity);
    }

    public void addCommand(String command) {
        commands.offer(command);
    }

    public void printCommands() {
        for(int i = 0; i < capacity; i++) {
            System.out.println(commands.poll());
        }
    }

    public static void run() {
        Scanner scanner = new Scanner(System.in);

        int numberOfCommands = scanner.nextInt();
        int CLICapacity = scanner.nextInt();

        BoundedCommandsQueue commandsQueue = new BoundedCommandsQueue(CLICapacity);

        scanner.nextLine();

        for(int i = 0; i < numberOfCommands; i++) {
            commandsQueue.addCommand(scanner.nextLine());
        }

        commandsQueue.printCommands();
    }
}

class FileSystem {
    private static DoubleHashSet<String> storage = new DoubleHashSet<>(1);


    private static QueuedBoundedStack<BackUp> backUps = new QueuedBoundedStack<>( 1);

    public static void init(int maxFilesDirsCount) {
        storage = new DoubleHashSet<>(maxFilesDirsCount);
        backUps = new QueuedBoundedStack<>(100);
    }

    public static void init(int maxFilesDirsCount, int maxBackUpDepth) {
        storage = new DoubleHashSet<>(10000);
        backUps = new QueuedBoundedStack<>(maxBackUpDepth - 1);
    }

    public static void create(String name) throws CreationException {


        try {
            storeStage();

            storage.add(name);
        } catch (RuntimeException e) {
            backUps.pop();
            throw new CreationException("NEW " + name);
        }
    }

    public static void remove(String name) throws RemoveException {
        try {
            storeStage();

            storage.remove(name);
        } catch (RuntimeException e) {
            backUps.pop();

            throw new RemoveException("REMOVE " + name);
        }
    }

    public static void makeBackUp() {
        BackUp previousStage = backUps.pop();

        storage = previousStage.getStorage();
    }

    public static void displayAllFilesAndDirs() {
        storage.print();
        System.out.println();
    }

    public static int countOfBackups() {
        return backUps.size();
    }

    private static void storeStage() {
        BackUp currentStage = new BackUp();

        backUps.push(currentStage);
    }

    static private class BackUp {
        private final DoubleHashSet<String> storage;

        public BackUp() {
            this.storage = FileSystem.storage.copy();
        }

        public DoubleHashSet<String> getStorage() {
            return storage;
        }

    }

    static class CreationException extends RuntimeException {
        public CreationException(String message) {
            super(message);
        }
    }

    static class RemoveException extends RuntimeException {
        public RemoveException(String message) {
            super(message);
        }
    }
}

class DoubleHashSet<T> implements ISet<T> {
    private static final Object DEFUNCT = "DEFUNCT";

    private final int PRIME;

    private T[] hashTable;
    private int capacity;
    private int size = 0;

    public DoubleHashSet(int capacity) {
        this.capacity = capacity;
        this.hashTable = (T[]) new Object[capacity];
        this.PRIME = 3;
    }

    @Override
    public void add(T item) {
        int index;

        for(int i = 0; i < capacity - 1; i++) {
            index = (hash1(item) + i * hash2(item)) % capacity;

            if (hashTable[index] == null) {
                hashTable[index] = item;
                size++;

                return;
            } else {
                if (hashTable[index].equals(item))
                    break;
            }
        }

        throw new RuntimeException();
    }

    @Override
    public void remove(T item) {
        int index;

        for(int i = 0; i < capacity - 1; i++) {
            index = (hash1(item) + i * hash2(item)) % capacity;

            if (hashTable[index] == null) break;

            if (hashTable[index].equals(item)) {
                hashTable[index] = (T) DEFUNCT;
                size--;

                return;
            }
        }

        throw new RuntimeException();
    }

    @Override
    public boolean contains(T item) {
        int index;

        for(int i = 0; i < capacity - 1; i++) {
            index = (hash1(item) + i * hash2(item)) % capacity;

            if (hashTable[index] == null) return false;

            if (hashTable[index].equals(item)) return true;
        }

        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    public int get(T item) {
        int index;

        for(int i = 0; i < capacity - 1; i++) {
            index = (hash1(item) + i * hash2(item)) % capacity;

            if (hashTable[index] == null) return -1;

            if (hashTable[index].equals(item)) return index;
        }

        return -1;
    }

    public void print() {
        for (int i = 0; i < capacity; i++) {
            if (hashTable[i] != null && !hashTable[i].equals(DEFUNCT))
                System.out.print(hashTable[i] + " ");
        }
    }

    public DoubleHashSet<T> copy() {
        DoubleHashSet<T> copySet = new DoubleHashSet<>(capacity);

        copySet.hashTable = Arrays.copyOf(hashTable, hashTable.length);
        copySet.capacity = capacity;
        copySet.size = size;

        return copySet;
    }

    private int hash1(T val) {
        int hashVal = Math.abs(val.hashCode());

        return hashVal % capacity;
    }

    private int hash2(T val) {
        int hashVal = Math.abs(val.hashCode());

        return PRIME - hashVal % PRIME;
    }

    public int getClosestPrime() {
        for (int i = capacity - 1; i >= 1; i--) {
            int count = 0;

            for (int j = 2; j * j <= i; j++)

                if (i % j == 0){
                    count++;
                }

            if (count == 0) {
                return i;
            }
        }
        return 3;
    }
}

interface ISet<T> {
    void add(T item);// add item in the set

    void remove(T item);// remove an item from a set

    boolean contains(T item);// check if a item belongs to a set

    int size();// number of elements in a set

    boolean isEmpty();// check if the set is empty
}

class QueuedBoundedStack<T> implements IBoundedQueue<T> {
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
            int basisSize = basicQueue.size();

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

interface IBoundedQueue<T> {
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





