package deque;

import java.util.Iterator;

public class ArrayDeque<T> implements Iterable<T>, Deque<T>{
    public T[] items;
    public int start;
    public int end;
    public int length;

    public Iterator<T> iterator() {
        return new ADiterator();
    }

    private class ADiterator implements Iterator<T>{
        private int curIndex;

        public ADiterator() {
            curIndex = 0;
        }

        @Override
        public boolean hasNext() {
            return (curIndex < size());
        }

        @Override
        public T next() {
            T returned = items[start + curIndex];
            curIndex++;
            return returned;
        }
    }

    @Override
    public boolean equals (Object other){
        if (this == other) return true;
        if (other == null) return false;
        if (this.getClass() != other.getClass()) return false;
        if (this.size() != ((ArrayDeque<?>) other).size()) return false;
        for (int i = 0; i < size(); i++){
            if (this.get(i) != ((ArrayDeque) other).get(i)) {
                return false;
            }
        }
        return true;
    }

    public ArrayDeque() {
        items = (T[]) new Object[8];
        start = 4;
        end = 3;
        length = 8;
    }

    public int size() {
        return end - start + 1;
    }

    public void extend() {
        int new_len = length * 2;
        T[] new_items = (T[]) new Object[new_len];
        System.arraycopy(items, start, new_items,start+ new_len / 4, end - start + 1);
        items = new_items;
        start += new_len / 4;
        end += new_len / 4;
        length *= 2;
    }

    public void shrink() {
        int new_len = length / 2;
        T[] new_items = (T[]) new Object[new_len];
        System.arraycopy(items, start, new_items, 1, end - start + 1);
        items = new_items;
        end = size();
        start = 1;
        length /= 2;
    }

    public void addFirst(T x) {
        if (start == 0){
            extend();
        }
        start--;
        items[start] = x;
    }

    public void addLast(T x) {
        if (end == length - 1) {
            extend();
        }
        end++;
        items[end] = x;
    }

    public void printDeque() {
        for (int i = start; i <= end; i++) {
            System.out.print(items[i] + " ");
        }
        System.out.println();
    }

    public boolean isEmpty() {
        return (start - end) == 1;
    }

    public T removeFirst() {
        if (!isEmpty()) {
            T removed = items[start];
            items[start] = null;
            start++;
            if (end - start + 1 < length / 2 - 1) {
                shrink();
            }
            return removed;
        }
        return null;
    }

    public T removeLast() {
        if (!isEmpty()) {
            T removed = items[end];
            items[end] = null;
            end--;
            if (end - start + 1 < length / 2 - 1){
                shrink();
            }
            return removed;
        }
        return null;
    }

    public T get(int index) {
        if (index >= size()) return null;
        return items[start + index];
    }
}
