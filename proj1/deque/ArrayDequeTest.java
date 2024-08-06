package deque;

import edu.princeton.cs.algs4.StdRandom;
import jh61b.junit.In;
import org.junit.Test;

import java.util.Optional;

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
        ad1.addFirst(1);
        ad1.addFirst(0);
        ad1.addLast(2);
        ad1.addLast(3);
        ad1.printDeque();
        assertFalse(ad1.isEmpty());
        ad1.removeFirst();
        ad1.removeLast();
        ad1.removeFirst();
        ad1.removeLast();
        ad1.removeFirst();
        ad1.removeLast();
        System.out.println(ad1.size());
//        System.out.println(ad1.get(1));
        ad1.printDeque();
        ad1.removeFirst();
        ad1.removeLast();
//        ad1.printDeque();
//        assertTrue(ad1.isEmpty());
        ad1.addFirst(1);
        ad1.addFirst(0);
        ad1.addLast(2);
        ad1.addLast(3);
        ad1.printDeque();
        System.out.println(ad1.size());
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

    @Test
    public void test6() {
        ArrayDeque<Integer> ad6 =new ArrayDeque<>();
        for (int i = 0; i < 10; i++){
            ad6.addLast(i);
        }
        for (int i = 0; i < 9; i++){
            ad6.removeFirst();
        }
        assertTrue(ad6.get(0)==9);
    }

    @Test
    public void randomizedTest() {
        ArrayDeque<Integer> arrayDeque = new ArrayDeque<>();

        int N = 100000000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 6);
            if (operationNumber == 0) {
                int randVal = StdRandom.uniform(0, 100);
                arrayDeque.addFirst(randVal);
            } else if (operationNumber == 1) {
                int randVal = StdRandom.uniform(0, 100);
                arrayDeque.addLast(randVal);
            } else if (arrayDeque.size() == 0) {
                assertTrue(arrayDeque.isEmpty());
            } else if (operationNumber == 2) {
                assertTrue(arrayDeque.size() > 0);
            } else if (operationNumber == 3) {
                arrayDeque.removeFirst();
            } else if (operationNumber == 4) {
                arrayDeque.removeLast();
            } else if (operationNumber == 5) {
                int randIndex = StdRandom.uniform(0, arrayDeque.size());
                arrayDeque.get(randIndex);
            }
        }
    }
}
