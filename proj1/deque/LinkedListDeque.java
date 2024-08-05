package deque;

import java.util.Iterator;

public class LinkedListDeque<T> {
    private static class node<T>{
        public T cur;
        public node<T> next;
        public node<T> prev;

        public node(T current, node<T> pre, node<T> nex){
            cur = current;
            next = nex;
            prev = pre;
        }

        public node(node<T> pre, node<T> nex){
            next = nex;
            prev = pre;
        }
    }

    public int size;
    public node<T> sentinel;

    public LinkedListDeque() {
        sentinel = new node<>(null, null);
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
        size = 0;
    }

    public void addFirst(T x) {
        node<T> tmp = new node<>(x, sentinel, sentinel.next);
        sentinel.next.prev = tmp;
        sentinel.next = tmp;
        size++;
    }

    public void addLast(T x) {
        node<T> tmp = new node<>(x, sentinel.prev, null);
        sentinel.prev.next = tmp;
        sentinel.prev = tmp;
        size++;
    }

    public void printDeque() {
        node<T> tmp = sentinel.next;
        for (int i = 0; i < size; i++) {
            System.out.print(tmp.cur + " ");
            tmp = tmp.next;
        }
        System.out.println();
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public T removeFirst() {
        if (sentinel.next.cur != null){
            T removed = sentinel.next.cur;
            sentinel.next = sentinel.next.next;
            sentinel.next.prev = sentinel;
            size--;
            return removed;
        }
        else return null;
    }

    public T removeLast() {
        if (sentinel.prev.cur != null) {
            T removed = sentinel.prev.cur;
            sentinel.prev = sentinel.prev.prev;
            sentinel.prev.next = sentinel;
            size--;
            return removed;
        }
        else return null;
    }

    public T get(int index) {
        if (index < size){
            node<T> tmp = sentinel;
            for (int cur_index = 0; cur_index <= index; cur_index ++) {
                tmp = tmp.next;
            }
            return tmp.cur;
        }
        else return null;
    }

    //TODO: iterator, equals
}
