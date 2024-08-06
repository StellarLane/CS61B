package deque;

public interface Deque<T> {
    void addFirst(T x);
    void addLast(T x);
    void printDeque(T x);
    void printdeque();
    boolean isempty();
    T removeFirst();
    T removeLast();
    T get(int index);
    boolean equals(Object other);
}
