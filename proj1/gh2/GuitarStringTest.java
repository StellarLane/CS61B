package gh2;
import org.junit.Test;
import static org.junit.Assert.*;

public class GuitarStringTest {
    @Test
    public void test1() {
        GuitarString test = new GuitarString(4410);
        test.getBuffer().printDeque();
        test.pluck();
        System.out.println(test.getBuffer().size());
        test.getBuffer().printDeque();
        test.tic();
        test.getBuffer().printDeque();
        System.out.println(test.sample());
    }
}
