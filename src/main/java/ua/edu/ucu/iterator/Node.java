package ua.edu.ucu.iterator;

class Node<T> {
    Node<T> next;
    T value;


    Node(T e) {
        this.value = e;
        this.next = null;
    }
}

