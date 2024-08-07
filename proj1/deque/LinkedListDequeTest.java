package deque;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;

import static org.junit.Assert.*;


/** Performs some basic linked list tests. */
public class LinkedListDequeTest {
    @Test
    public void test1() {
        LinkedListDeque<String> lld1 = new LinkedListDeque<>();
        assertTrue(lld1.isEmpty());
        lld1.addFirst("hello");
        lld1.addLast("world");
        assertEquals(lld1.size(), 2);
        assertFalse(lld1.isEmpty());
        lld1.printDeque();
        System.out.print(lld1.get(0));
        System.out.print(lld1.get(1));
        lld1.removeFirst();
        lld1.removeLast();
        assertTrue(lld1.isEmpty());
        lld1.printDeque();
    }

    @Test
    /** Adds a few things to the list, checking isEmpty() and size() are correct,
     * finally printing the results.
     *
     * && is the "and" operation. */
    public void addIsEmptySizeTest() {
        LinkedListDeque<String> lld1 = new LinkedListDeque<String>();

		assertTrue("A newly initialized LLDeque should be empty", lld1.isEmpty());
		lld1.addFirst("front");

		// The && operator is the same as "and" in Python.
		// It's a binary operator that returns true if both arguments true, and false otherwise.
        assertEquals(1, lld1.size());
        assertFalse("lld1 should now contain 1 item", lld1.isEmpty());

		lld1.addLast("middle");
		assertEquals(2, lld1.size());

		lld1.addLast("back");
		assertEquals(3, lld1.size());

		System.out.println("Printing out deque: ");
		lld1.printDeque();
    }

    @Test
    /** Adds an item, then removes an item, and ensures that dll is empty afterwards. */
    public void addRemoveTest() {

        LinkedListDeque<Integer> lld1 = new LinkedListDeque<Integer>();
		// should be empty
		assertTrue("lld1 should be empty upon initialization", lld1.isEmpty());

		lld1.addFirst(10);
		// should not be empty
		assertFalse("lld1 should contain 1 item", lld1.isEmpty());

		lld1.removeFirst();
		// should be empty
		assertTrue("lld1 should be empty after removal", lld1.isEmpty());
    }

    @Test
    /* Tests removing from an empty deque */
    public void removeEmptyTest() {

        LinkedListDeque<Integer> lld1 = new LinkedListDeque<>();
        lld1.addFirst(3);

        lld1.removeLast();
        lld1.removeFirst();
        lld1.removeLast();
        lld1.removeFirst();

        int size = lld1.size();
        String errorMsg = "  Bad size returned when removing from empty deque.\n";
        errorMsg += "  student size() returned " + size + "\n";
        errorMsg += "  actual size() returned 0\n";

        assertEquals(errorMsg, 0, size);
    }

    @Test
    /* Check if you can create LinkedListDeques with different parameterized types*/
    public void multipleParamTest() {

        LinkedListDeque<String>  lld1 = new LinkedListDeque<String>();
        LinkedListDeque<Double>  lld2 = new LinkedListDeque<Double>();
        LinkedListDeque<Boolean> lld3 = new LinkedListDeque<Boolean>();

        lld1.addFirst("string");
        lld2.addFirst(3.14159);
        lld3.addFirst(true);

        String s = lld1.removeFirst();
        double d = lld2.removeFirst();
        boolean b = lld3.removeFirst();
    }

    @Test
    /* check if null is return when removing from an empty LinkedListDeque. */
    public void emptyNullReturnTest() {
        LinkedListDeque<Integer> lld1 = new LinkedListDeque<Integer>();

        boolean passed1 = false;
        boolean passed2 = false;
        assertEquals("Should return null when removeFirst is called on an empty Deque,", null, lld1.removeFirst());
        assertEquals("Should return null when removeLast is called on an empty Deque,", null, lld1.removeLast());
    }

    @Test
    /* Add large number of elements to deque; check if order is correct. */
    public void bigLLDequeTest() {
        LinkedListDeque<Integer> lld1 = new LinkedListDeque<Integer>();
        for (int i = 0; i < 1000000; i++) {
            lld1.addLast(i);
        }

        for (double i = 0; i < 500000; i++) {
            assertEquals("Should have the same value", i, (double) lld1.removeFirst(), 0.0);
        }

        for (double i = 999999; i > 500000; i--) {
            assertEquals("Should have the same value", i, (double) lld1.removeLast(), 0.0);
        }
    }

    @Test
    public void test2() {
        LinkedListDeque<Integer> lld2 = new LinkedListDeque<>();
        for (int i = 0; i < 10; i++) {
            lld2.addLast(i);
        }
        for (Object i : lld2){
            System.out.print(i + " ");
        }
    }

    @Test
    public void test3() {
        LinkedListDeque<Integer> lld3_1 = new LinkedListDeque<>();
        LinkedListDeque<Integer> lld3_2 = new LinkedListDeque<>();
        LinkedListDeque<Integer> lld3_3 = new LinkedListDeque<>();
        for (int i = 0; i < 10; i++) {
            lld3_1.addLast(i);
            lld3_2.addLast(i);
            lld3_3.addLast(i);
            assertEquals(lld3_1.get(i), lld3_2.getRecursive(i));
        }
        lld3_3.removeLast();
        assertEquals(lld3_1, lld3_2);
        assertNotEquals(lld3_1, lld3_3);
    }

    @Test
    public void test4() {
        LinkedListDeque<Integer> lld4 = new LinkedListDeque<>();
        ArrayDeque<Integer> ad4 = new ArrayDeque<>();
        lld4.addLast(1);
        ad4.addLast(1);
        lld4.addLast(2);
        assertFalse(lld4.equals(ad4));
        lld4.removeLast();
        assertTrue(lld4.equals(ad4));
    }

    @Test
    public void randomizedTest() {
        LinkedListDeque<Integer> linkedListDequeTest = new LinkedListDeque<>();

        int N = 100000000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 6);
            if (operationNumber == 0) {
                int randVal = StdRandom.uniform(0, 100);
                linkedListDequeTest.addFirst(randVal);
            } else if (operationNumber == 1) {
                int randVal = StdRandom.uniform(0, 100);
                linkedListDequeTest.addLast(randVal);
            } else if (linkedListDequeTest.size() == 0) {
                assertTrue(linkedListDequeTest.isEmpty());
            } else if (operationNumber == 2) {
                assertTrue(linkedListDequeTest.size() > 0);
            } else if (operationNumber == 3) {
                linkedListDequeTest.removeFirst();
            } else if (operationNumber == 4) {
                linkedListDequeTest.removeLast();
            } else if (operationNumber == 5) {
                int randIndex = StdRandom.uniform(0, linkedListDequeTest.size());
                linkedListDequeTest.get(randIndex);
            }
        }
    }
}
