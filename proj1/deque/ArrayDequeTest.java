package deque;

import jh61b.junit.In;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArrayDequeTest {
    @Test
    public void test1() {
        ArrayDeque<Integer> ad1 = new ArrayDeque<>();
        assertTrue(ad1.isEmpty());
        ad1.addFirst(1);
        ad1.addFirst(0);
        ad1.addLast(2);
        ad1.addLast(3);
        ad1.printDeque();
        assertFalse(ad1.isEmpty());
        ad1.removeFirst();
        ad1.removeLast();
        System.out.println(ad1.get(0));
        System.out.println(ad1.get(1));
        ad1.printDeque();
        ad1.removeFirst();
        ad1.removeLast();
        ad1.printDeque();
        assertTrue(ad1.isEmpty());
    }

    @Test
    public void test2() {
        ArrayDeque<Integer> ad2 = new ArrayDeque<>();
        for (int i = 0; i < 100; i++) {
            ad2.addFirst(1);
            ad2.addLast(3);
        }
    }
}
