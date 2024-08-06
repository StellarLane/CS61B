package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {
    private Comparator<T> MAComparator;
    public MaxArrayDeque(Comparator<T> c) {
        MAComparator = c;
    }

    public T max() {
        if (isEmpty())return null;
        int maxIndex = 0;
        for (int i = 0; i < size(); i++) {
            if (MAComparator.compare(this.get(i),this.get(maxIndex)) > 0) maxIndex = i;
        }
        return this.get(maxIndex);
    }

    public T max(Comparator NewComparator){
        if (isEmpty())return null;
        int maxIndex = 0;
        for (int i = 0; i < size(); i++) {
            if (NewComparator.compare(this.get(i),this.get(maxIndex)) > 0) maxIndex = i;
        }
        return this.get(maxIndex);
    }
}
