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
            ad2.addFirst(-i);
            ad2.addLast(i);
            System.out.println(ad2.get(100));
        }
    }

    @Test
    public void test3() {
        ArrayDeque<Integer> ad3 = new ArrayDeque<>();
        for (int i = 0; i < 20; i++){
            ad3.addFirst(-i);
            ad3.addLast(i);
        }
        for (int i = 0; i < 10; i++){
            ad3.removeFirst();
            ad3.removeFirst();
            System.out.println(ad3.size());
        }
    }

    @Test
    public void test4() {
        ArrayDeque<Integer> ad3_1 = new ArrayDeque<>();
        ArrayDeque<Integer> ad3_2 = new ArrayDeque<>();
        ArrayDeque<Integer> ad3_3 = new ArrayDeque<>();
        for (int i = 0; i < 10; i++) {
            ad3_1.addLast(i);
            ad3_2.addLast(i);
            ad3_3.addLast(i);
        }
        ad3_3.removeLast();
        assertEquals(ad3_2, ad3_1);
        assertNotEquals(ad3_3, ad3_1);
    }

    @Test
    public void test5() {
        ArrayDeque<Integer> ad5 = new ArrayDeque<>();
        for (int i = 0; i < 10; i++) {
            ad5.addLast(i);
        }
        for (Object i: ad5){
            System.out.print(i + " ");
        }
    }
}
