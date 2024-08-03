package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.checkerframework.checker.units.qual.A;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {
  // YOUR TESTS HERE
    @Test
    public void testThreeAddThreeRemove(){
        AListNoResizing<Integer> Cor = new AListNoResizing<>();
        BuggyAList<Integer> Bug = new BuggyAList<>();
        for (int i = 4; i <= 6; i++){
            Cor.addLast(i);
            Bug.addLast(i);
        }
        assertEquals(Bug.size(),Cor.size());
        for (int i = 0; i < 3; i++){
            assertEquals(Bug.removeLast(),Cor.removeLast());
        }
    }

    @Test
    public void randomizedTest_0(){
        AListNoResizing<Integer> Cor = new AListNoResizing<>();
        int N = 500;
        for (int n = 0; n <= N; n++){
            int operationNumber = StdRandom.uniform(0,2);
            if (operationNumber == 0){
                int randVal = StdRandom.uniform(0,100);
                Cor.addLast(randVal);
                System.out.println("addlast" + "(" + randVal + ")");
            } else if (operationNumber == 1) {
                int size = Cor.size();
                System.out.println("size: " + size);
            }
        }
    }

    @Test
    public void randomizedTest(){
        AListNoResizing<Integer> Cor = new AListNoResizing<>();
        BuggyAList<Integer> Bug = new BuggyAList<>();
        int N = 5000;
        for (int n = 0; n<=N; n++){
            int operationNumber = StdRandom.uniform(0,4);
            switch (operationNumber){
                case (0):
                    int randVal = StdRandom.uniform(0,100);
                    Cor.addLast(randVal);
                    Bug.addLast(randVal);
                    break;
                case (1):
                    int sizeCor = Cor.size();
                    int sizeBug = Bug.size();
                    assertEquals(sizeCor, sizeBug);
                    break;
                case (2):
                    if (Cor.size() == 0) continue;
                    assertEquals(Cor.getLast(), Bug.getLast());
                    break;
                case (3):
                    if (Cor.size() == 0) continue;
                    assertEquals(Cor.removeLast(), Bug.removeLast());
            }
        }
    }
}