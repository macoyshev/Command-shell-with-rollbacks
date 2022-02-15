package com.company;

import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        CommandShellWithoutRollbacks.run();
    }
}

class CommandShellWithRollbacks {
    private static DoublyLinkedCircularBoundedQueue<String> commands;
    public static void run() {
        readData();
        executeAllCommands();
    }

    private static void readData() {
        Scanner scanner = new Scanner(System.in);

        int countOfCommands = scanner.nextInt();
        int backUpsDepth = scanner.nextInt();
        scanner.nextLine();

        FileSystem.setBackUpDepth(backUpsDepth);
        commands = new DoublyLinkedCircularBoundedQueue<>(countOfCommands);

        for(int i = 0; i < countOfCommands; i++) {
            commands.offer(scanner.nextLine());
        }
    }

    private static void executeAllCommands() {
        for(int i = 0; i < commands.capacity(); i++) {

            String[] command = commands.poll().split(" ");

            String operation = null;
            String arg = null;

            try {
                operation = command[0];

                if (command.length == 2)
                    arg = command[1];

                switch (operation) {
                    case "NEW":
                        if (FileSystem.isDirName(arg)) FileSystem.createDir(arg);
                        else FileSystem.createFile(arg);

                        break;
                    case "REMOVE":
                        if (FileSystem.isDirName(arg)) FileSystem.removeDir(arg);
                        else FileSystem.removeFile(arg);

                        break;
                    case "LIST":
                        FileSystem.displayAllFilesAndDirs();

                        break;

                    case "UNDO":
                        if (arg != null) {
                            int count = Integer.parseInt(arg);

                            if (count >= FileSystem.getCurrentDepth())
                                throw new RuntimeException("UNDO " + count);

                            for(int j = 0; j < count; j++) {
                                FileSystem.makeBackUp();
                            }
                        } else {
                            if (FileSystem.getCurrentDepth() == 1)
                                throw new RuntimeException("UNDO");

                            FileSystem.makeBackUp();
                        }
                        break;
                    default:
                        if (arg != null)
                            throw new RuntimeException(operation + " " + arg);
                        else
                            throw new RuntimeException(operation);
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
        readData();
        executeAllCommands();
    }

    private static void readData() {
        Scanner scanner = new Scanner(System.in);

        int countOfCommands = scanner.nextInt();
        scanner.nextLine();
        commands = new DoublyLinkedCircularBoundedQueue<>(countOfCommands);

        for(int i = 0; i < countOfCommands; i++) {
            commands.offer(scanner.nextLine());
        }
    }

    private static void executeAllCommands() {
        for(int i = 0; i < commands.capacity(); i++) {

            String[] command = commands.poll().split(" ");

            String operation = null;
            String arg = null;

            try {
                if (command.length > 2) {
                    String errMes = Arrays.toString(command)
                            .replaceAll(",","")
                            .replace("[","")
                            .replace("]","");

                    throw new RuntimeException(errMes);
                }

                operation = command[0];

                if (command.length == 2)
                    arg = command[1];

                switch (operation) {
                    case "NEW":
                        if (arg == null) throw new RuntimeException("NEW");

                        if (FileSystem.isDirName(arg)) FileSystem.createDir(arg);
                        else FileSystem.createFile(arg);

                        break;
                    case "REMOVE":
                        if (arg == null) throw new RuntimeException("REMOVE");

                        if (FileSystem.isDirName(arg)) FileSystem.removeDir(arg);
                        else FileSystem.removeFile(arg);

                        break;
                    case "LIST":
                        if (arg != null) throw new RuntimeException("LIST " + arg);

                        FileSystem.displayAllFilesAndDirs();

                        break;
                    default:
                        if (arg != null)
                            throw new RuntimeException(operation + " " + arg);
                        else
                            throw new RuntimeException(operation);
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
    private static final int MAX_FILES_AND_DIRS_COUNT = 10000;
    private static final int MAX_BACKUP_DEPTH = 100;

    private static DoubleHashSet<String> storage = new DoubleHashSet<>(MAX_FILES_AND_DIRS_COUNT);

    private static QueuedBoundedStack<BackUp> backUps = new QueuedBoundedStack<>(MAX_BACKUP_DEPTH);

    public static boolean isDirName(String dir) {
        return dir.charAt(dir.length() - 1) == '/';
    }

    public static void createDir(String newDirName) throws DirCreationException {
        if (storage.contains(newDirName))
            throw new DirCreationException("NEW " + newDirName);

        storeStage();

        storage.add(newDirName);
    }

    public static void createFile(String newFileName) throws FileCreationException {
        if (storage.contains(newFileName))
            throw new FileCreationException("NEW " + newFileName);

        storeStage();

        storage.add(newFileName);
    }

    public static void removeDir(String dirName) throws DirRemovingException {
        if (!storage.contains(dirName))
            throw new DirRemovingException("REMOVE " + dirName);

        storeStage();

        storage.remove(dirName);

    }

    public static void removeFile(String fileName) throws FileRemovingException {
        if (!storage.contains(fileName))
            throw new FileRemovingException("REMOVE " + fileName);

        storeStage();

        storage.remove(fileName);
    }

    public static void makeBackUp() {
        BackUp previousStage = backUps.pop();

        storage = previousStage.getStorage();
    }

    public static void displayAllFilesAndDirs() {
        StringBuilder res = new StringBuilder();
        DoublyLinkedCircularBoundedQueue<String> storageValues = storage.getValues();


        while (!storageValues.isEmpty()){
            res.append(storageValues.poll());
            res.append(" ");
        }

        if (res.length() != 0)
            System.out.println(res);
    }

    public static int getCurrentDepth() {
        return backUps.size();
    }

    public static void setBackUpDepth(int size) {
        backUps = new QueuedBoundedStack<>(size);
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

    static class BackUpException extends RuntimeException {
        public BackUpException(String message) {
            super(message);
        }
    }

    static class DirCreationException extends RuntimeException {
        public DirCreationException(String message) {
            super(message);
        }
    }

    static class FileCreationException extends RuntimeException {
        public FileCreationException(String message) {
            super(message);
        }
    }

    static class DirRemovingException extends RuntimeException {
        public DirRemovingException(String message) {
            super(message);
        }
    }

    static class FileRemovingException extends RuntimeException {
        public FileRemovingException(String message) {
            super(message);
        }
    }
}

class DoubleHashSet<T> implements ISet<T> {
    private T[] hashTable;

    private int capacity;
    private int PRIME = 3 ;
    private int size = 0;

    public DoubleHashSet(int capacity) {
        this.capacity = capacity;
        this.hashTable = (T[]) new Object[capacity];
    }

    @Override
    public void add(T item) {
        if (size == capacity) throw new IllegalStateException();

        int index = getIndex(item);

        if (index != -1 && hashTable[index] == null) {
            hashTable[index] = item;
            size += 1;
        }
    }

    @Override
    public void remove(T item) {
        if (size == 0) return;

        int index = getIndex(item);

        if (index != -1 && hashTable[index] != null) {
            hashTable[index] = null;
            size -= 1;
        }
    }

    @Override
    public boolean contains(T item) {
        int index = getIndex(item);

        return index != -1 && hashTable[index] != null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < capacity; i++) {
            if (hashTable[i] != null) return false;
        }

        return true;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < capacity; i++) {
            if (hashTable[i] != null) {
                stringBuilder.append(hashTable[i]);
                stringBuilder.append(" ");
            }
        }

        return stringBuilder.toString();
    }

    public DoubleHashSet<T> copy() {
        DoubleHashSet<T> copySet = new DoubleHashSet<>(this.capacity);

        copySet.hashTable = Arrays.copyOf(this.hashTable, this.hashTable.length);
        copySet.capacity = this.capacity;
        copySet.PRIME = this.PRIME;
        copySet.size = size;

        return copySet;
    }

    public DoublyLinkedCircularBoundedQueue<T> getValues() {
        DoublyLinkedCircularBoundedQueue<T> res = new DoublyLinkedCircularBoundedQueue<>(size);

        for(int i = 0; i < capacity; i++) {
            if (hashTable[i] != null)
                res.offer(hashTable[i]);
        }

        return res;
    }

    private int getIndex(T item) {
        int index;

        for (int i = 0; i < capacity; i++) {
            index = (hash1(item) + i * hash2(item)) % capacity;

            if (hashTable[index] != null) {
                if (hashTable[index].equals(item))
                    return index;
            }

            if (hashTable[index] == null) return index;
        }

        return -1;
    }

    private int hash1(T val) {
        int index = Math.abs(val.hashCode());
        return index % capacity;
    }

    private int hash2(T val) {
        int index = Math.abs(val.hashCode());
        return PRIME - index % PRIME;
    }

    private int getClosestPrime(int number) {
        int prime = number - 1;
        while (!isPrime(prime)) prime--;

        return prime;
    }

    private boolean isPrime(int number) {
        if (number == 1)
            return false;

        if (number <= 3)
            return true;

        for (int i = 2; i < Math.sqrt(number); i++) {
            if (number % i == 0) return false;
        }

        return false;
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





