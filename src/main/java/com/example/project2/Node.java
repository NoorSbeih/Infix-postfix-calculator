package com.example.project2;

public class Node {
    private final Object data;
    private int next;

    Node(Object data, int next){
        this.data=data;
        this.next=next;
    }

    public Object getData() {
        return data;
    }

    public int getNext() {
        return next;
    }

    public void setNext(int next) {
        this.next = next;
    }
}
