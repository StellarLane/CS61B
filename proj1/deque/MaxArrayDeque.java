package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {
    private Comparator<T> maComparator;
    public MaxArrayDeque(Comparator<T> c) {
        maComparator = c;
    }

    public T max() {
        if (isEmpty()) {
            return null;
        }
        int maxIndex = 0;
        for (int i = 0; i < size(); i++) {
            if (maComparator.compare(this.get(i), this.get(maxIndex)) > 0) {
                maxIndex = i;
            }
        }
        return this.get(maxIndex);
    }

    public T max(Comparator<T> newComparator) {
        if (isEmpty()) {
            return null;
        }
        int maxIndex = 0;
        for (int i = 0; i < size(); i++) {
            if (newComparator.compare(this.get(i), this.get(maxIndex)) > 0) {
                maxIndex = i;
            }
        }
        return this.get(maxIndex);
    }
}
