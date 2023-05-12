package com.example.project2;

public class CursorStack {
    Node[] cursorArray = new Node[150];

    public CursorStack(){
        this.initialize();
    }

    private void initialize() {
        for(int i =0;i<cursorArray.length-1;i++)
            cursorArray[i] = new Node(null,i+1);
        cursorArray[cursorArray.length-1]= new Node(null,0);
    }
    public int malloc(){
        int p = cursorArray[0].getNext();
        cursorArray[0].setNext(cursorArray[p].getNext());
        return p;
    }
    public boolean isNull(int l){
        return cursorArray[l]==null;
    }
    public boolean isEmpty(int l){
        return cursorArray[l].getNext()==0;
    }


    public void free(int p) {
        cursorArray[p] = new Node(null, cursorArray[0].getNext());
        cursorArray[0].setNext(p);
    }
    public void empty(int p){ //delete all elements in stack
        while(getPeek(p)!=null)
            pop(p);
    }
    public Object getPeek(int l){
        int peek = cursorArray[l].getNext();
        if(peek!=0)
            return cursorArray[peek].getData();
        else return null;
    }
    public int createStack(){
        int l = malloc();
        if(l==0)
            System.out.println("Cursor is full!");
        else cursorArray[l] = new Node("-",0);
        return l;
    }
    public void push(Object data, int l){  //same as insert at head
        if(isNull(l))
            return;
        int p = malloc();
        if(p!=0){
            cursorArray[p] = new Node( data, cursorArray[l].getNext());
            cursorArray[l].setNext(p);

        }
        else System.out.println("Out of space!");



    }
    public Node pop(int l){
        if(isNull(l))
            return null;
        else{
            int p = cursorArray[l].getNext();
            Object data = cursorArray[p].getData();
            cursorArray[l].setNext(cursorArray[p].getNext());
            free(p);
            return new Node(data,0);


        }
    }


}

