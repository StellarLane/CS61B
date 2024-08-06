package deque;

import java.util.Iterator;

public class ArrayDeque<T> implements Iterable<T>, Deque<T> {
    private T[] items;
    private int start;
    private int end;
    private int length;

    public Iterator<T> iterator() {
        return new ADiterator();
    }

    private class ADiterator implements Iterator<T> {
        private int curIndex;

        ADiterator() {
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
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null) {
            return false;
        }
        if (this.getClass() != other.getClass()) {
            return false;
        }
        if (this.size() != ((ArrayDeque<?>) other).size()) {
            return false;
        }
        for (int i = 0; i < size(); i++) {
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

    private void extend() {
        int newLen = length * 2;
        T[] newItems = (T[]) new Object[newLen];
        System.arraycopy(items, start, newItems, start + newLen / 4, end - start + 1);
        items = newItems;
        start += newLen / 4;
        end += newLen / 4;
        length *= 2;
    }

    private void shrink() {
        int newLen = length / 2;
        T[] newItems = (T[]) new Object[newLen];
        System.arraycopy(items, start, newItems, 1, end - start + 1);
        items = newItems;
        end = size();
        start = 1;
        length /= 2;
    }

    public void addFirst(T x) {
        if (start == 0) {
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
            if (end - start + 1 < length / 2 - 1) {
                shrink();
            }
            return removed;
        }
        return null;
    }

    public T get(int index) {
        if (index >= size()) {
            return null;
        }
        return items[start + index];
    }
}
