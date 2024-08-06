package deque;

import java.util.Iterator;

public class LinkedListDeque<T> implements Iterable<T>, Deque<T> {
    public Iterator<T> iterator() {
        return new LLDiterator();
    }

    private class LLDiterator implements Iterator<T> {
        private Node<T> curNode;
        private int curIndex;

        LLDiterator() {
            curNode = sentinel.next;
            curIndex = 0;
        }

        @Override
        public boolean hasNext() {
            return (curIndex < size);
        }

        @Override
        public T next() {
            T returned = curNode.cur;
            curNode = curNode.next;
            curIndex++;
            return returned;
        }
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null) {
            return false;
        }
        if (other.getClass() != this.getClass()) {
            return false;
        }
        if (((LinkedListDeque<?>) other).size() != this.size()) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            if (this.get(i) != ((LinkedListDeque) other).get(i)) {
                return false;
            }
        }
        return true;
    }


    private static class Node<T>{
        private T cur;
        private Node<T> next;
        private Node<T> prev;

        Node(T current, Node<T> pre, Node<T> nex) {
            cur = current;
            next = nex;
            prev = pre;
        }

        Node(Node<T> pre, Node<T> nex) {
            next = nex;
            prev = pre;
        }
    }

    private int size;
    private Node<T> sentinel;

    public LinkedListDeque() {
        sentinel = new Node<>(null, null);
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
        size = 0;
    }

    public void addFirst(T x) {
        Node<T> tmp = new Node<>(x, sentinel, sentinel.next);
        sentinel.next.prev = tmp;
        sentinel.next = tmp;
        size++;
    }

    public void addLast(T x) {
        Node<T> tmp = new Node<>(x, sentinel.prev, null);
        sentinel.prev.next = tmp;
        sentinel.prev = tmp;
        size++;
    }

    public void printDeque() {
        Node<T> tmp = sentinel.next;
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
        if (sentinel.next.cur != null) {
            T removed = sentinel.next.cur;
            sentinel.next = sentinel.next.next;
            sentinel.next.prev = sentinel;
            size--;
            return removed;
        }
        else {
            return null;
        }
    }

    public T removeLast() {
        if (sentinel.prev.cur != null) {
            T removed = sentinel.prev.cur;
            sentinel.prev = sentinel.prev.prev;
            sentinel.prev.next = sentinel;
            size--;
            return removed;
        }
        else {
            return null;
        }
    }

    public T get(int index) {
        if (index < size) {
            Node<T> tmp = sentinel;
            for (int cur_index = 0; cur_index <= index; cur_index ++) {
                tmp = tmp.next;
            }
            return tmp.cur;
        }
        else {
            return null;
        }
    }

    public T getRecursive(int index) {
        Node<T> tmp = sentinel.next;
        if (index < 0 || index >= size()) {
            return null;
        }
        return getRecursiveHelper(index, tmp);
    }


    private T getRecursiveHelper(int index, Node<T> helperList){
        if (index == 0) {
            return helperList.cur;
        }
        return getRecursiveHelper(--index, helperList.next);
    }
}
