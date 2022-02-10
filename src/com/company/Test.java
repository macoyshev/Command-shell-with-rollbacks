package com.company;


public class Test {
    public static void main(String[] args) {
        DoublyLinkedList<Integer> arr = new MyDoublyLinkedList<>(3);
        arr.addLast(1);
        arr.addLast(2);
        arr.addLast(3);
        arr.addLast(4);
        arr.addLast(5);

        System.out.println(arr);

        arr.addFirst(6);

        System.out.println(arr);

        arr.removeLast();

        System.out.println(arr);

        arr.removeFirst();

        System.out.println(arr);
    }
}
