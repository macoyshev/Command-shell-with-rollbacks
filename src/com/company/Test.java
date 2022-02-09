package com.company;


public class Test {
    public static void main(String[] args) {
        ArrayCircularBoundedQueue<String> arr = new ArrayCircularBoundedQueue<>(5);

        arr.offer("1");
        arr.offer("2");
        arr.offer("3");

        String a = arr.poll();
        String b = arr.peek();

        int c = arr.capacity();
    }
}
