package deque;

public class ArrayDeque<T> {
    public T[] items;
    public int start;
    public int end;
    public int length;

    public ArrayDeque() {
        items = (T[]) new Object[8];
        start = 4;
        end = 3;
        length = 8;
    }

    public void extend(int new_len){
        T[] new_items = (T[]) new Object[new_len];
        System.arraycopy(items, start, new_items,start+ new_len / 4, end - start + 1);
        items = new_items;
        start += new_len / 4;
        length *= 2;
    }

    public void addFirst(T x) {
        if (start == 0){
            extend(2 * length);
        }
        start--;
        items[start] = x;
    }

    public void addLast(T x) {
        if (end == length - 1) {
            extend(2 * length);
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
            return removed;
        }
        return null;
    }

    public T removeLast() {
        if (!isEmpty()) {
            T removed = items[end];
            items[end] = null;
            end--;
            return removed;
        }
        return null;
    }

    public T get(int index) {
        return items[start + index];
    }
}
