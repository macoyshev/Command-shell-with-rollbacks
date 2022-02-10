package com.company;


public class Test {
    public static void main(String[] args) {
        QueuedBoundedStack<Integer> arr = new QueuedBoundedStack<>(3);

        arr.push(1);
        arr.push(2);
        arr.push(3);
        arr.push(4);

        System.out.println(arr);
        System.out.println(arr.top());

        arr.flush();

        System.out.println(arr);
    }
}
