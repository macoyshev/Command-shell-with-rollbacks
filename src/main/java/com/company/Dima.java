package com.company;

import java.util.Scanner;

public class Dima {
    static boolean comparingList(String str){
        if ((str.charAt(0)=='L')&&(str.charAt(1)=='I')&&(str.charAt(2)=='S')&&(str.charAt(3)=='T')){
            return true;
        }
        else
            return false;
    }
    static boolean comparingNew(String str){
        if ((str.charAt(0)=='N')&&(str.charAt(1)=='E')&&(str.charAt(2)=='W')){return true;}
        else
            return false;
    }
    static boolean comparingRemove(String str){
        if ((str.charAt(0)=='R')&&(str.charAt(1)=='E')&&(str.charAt(2)=='M')
                &&(str.charAt(3)=='O')&&(str.charAt(4)=='V')&&(str.charAt(5)=='E')){return true;}
        else
            return false;
    }
    public static void main(String[] args) {

        int N;
        Scanner scanner = new Scanner(System.in);
        N = scanner.nextInt();
        scanner.nextLine();
        DHS set = new DHS(N);
        String str;
        String name = "";
        String[] strings = new String[N];
        int count = 0;
        for (int i = 0; i < N; i++){
            str = scanner.nextLine();

            name = "";
            if (comparingNew(str)){
                for (int j = 4;j < str.length();j++){
                    name = name + str.charAt(j);
                }
                if (set.contains(name)){
                    System.out.println("ERROR: cannot execute NEW "+name);
                }
                else
                {
                    set.add(name);
                    strings[count] = name;
                    count++;
                }
            }
            else
            if (comparingRemove(str)){
                for (int j = 7;j < str.length();j++){
                    name = name + str.charAt(j);
                }
                if (set.contains(name)){
                    set.remove(name);
                }
                else
                {
                    System.out.println("ERROR: cannot execute REMOVE "+name);
                }

            }
            else
            if (comparingList(str)){
                for (int q = 0;q < count;q++){
                    if (set.contains(strings[q])){
                        System.out.print(strings[q]+" ");
                    }
                }
            }

        }



    }
}



class DHS implements SE {

    Object hashtable[];
    int capacity;
    int size;
    int prime_number;
    int hashvalue1;
    int hashvalue2;
    int double_hash;
    int hashvalue;
    Object DEFUNCT = "DEFUNCT";
    DHS(int capacity){
        this.capacity = capacity;
        hashtable = new Object[capacity];
        size = 0;
        prime_number = 3;

    }

    @Override
    public void add(Object item) {
        size++;
        hashvalue = Math.abs(item.hashCode());
        hashvalue1 = hashvalue % capacity;
        hashvalue2 = (prime_number - (hashvalue % prime_number));
        double_hash = hashvalue1;
        int j = 1;
        while (hashtable[double_hash] != null){

            double_hash = (hashvalue1 + j * hashvalue2) % capacity;
            if (j > capacity -1  ) break;
            j++;
        }
        hashtable[double_hash] = item;
    }

    @Override
    public void remove(Object item) {
        hashvalue = Math.abs(item.hashCode());
        hashvalue1 = hashvalue % capacity;
        hashvalue2 = (prime_number - (hashvalue % prime_number));
        double_hash = hashvalue1;
        int j = 0;
        while ((hashtable[double_hash]!=null)&&(!hashtable[double_hash].equals(item)))
        {
            j++;
            double_hash = (hashvalue1 + j * hashvalue2) % capacity;
        }
        hashtable[double_hash] = DEFUNCT;
        size --;
    }

    @Override
    public boolean contains(Object item) {
        hashvalue = Math.abs(item.hashCode());
        hashvalue1 = hashvalue % capacity;
        hashvalue2 = (prime_number - (hashvalue % prime_number));
        double_hash = hashvalue1;
        int j = 0;
        while ((hashtable[double_hash]!=null)&&(!hashtable[double_hash].equals(item)))
        {
            j++;
            double_hash = (hashvalue1 + j * hashvalue2) % capacity;
        }
        if (hashtable[double_hash]!=null) {
            return hashtable[double_hash].equals(item);
        }
        else return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }
}
interface SE<T> {
    void add(T item);
    void remove(T item);
    boolean contains(T item);
    int size();
    boolean isEmpty();
}