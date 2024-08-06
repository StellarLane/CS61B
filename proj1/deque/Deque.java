package deque;

public interface Deque<T> {
    void addFirst(T x);
    void addLast(T x);
    void printDeque();
    default boolean isEmpty() {
        return size() == 0;
    }
    int size();
    T removeFirst();
    T removeLast();
    T get(int index);
    boolean equals(Object other);
}
