package deque;

import afu.org.checkerframework.checker.igj.qual.I;

public class LinkedListDeque<Item> {
    private static class node<Item>{
        public Item cur;
        public node<Item> next;
        public node<Item> prev;

        public node(Item current, node<Item> pre, node<Item> nex){
            cur = current;
            next = nex;
            prev = pre;
        }

        public node(node<Item> pre, node<Item> nex){
            next = nex;
            prev = pre;
        }
    }

    public int size;
    public node<Item> sentinel;

    public LinkedListDeque() {
        sentinel = new node<>(null, null);
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
        size = 0;
    }

    public void addFirst(Item x) {
        node<Item> tmp = new node<>(x, sentinel, sentinel.next);
        sentinel.next.prev = tmp;
        sentinel.next = tmp;
        size++;
    }

    public void addLast(Item x) {
        node<Item> tmp = new node<>(x, sentinel.prev, null);
        sentinel.prev.next = tmp;
        sentinel.prev = tmp;
        size++;
    }

    public void printDeque() {
        node<Item> tmp = sentinel.next;
        for (int i = 0; i < size; i++) {
            System.out.print(tmp.cur + " ");
            tmp = tmp.next;
        }
        System.out.println();
    }

}
