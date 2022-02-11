import com.company.DoubleHashSet;
import com.company.ISet;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DoubleHashSetTrivialTests {
    @Test
    public void dataStoreAndChecking(){
        ISet<Integer> set = new DoubleHashSet<Integer>(17);

        assertTrue(set.isEmpty());

        set.add(56);
        set.add(74);
        set.add(32);
        set.add(11);

        assertFalse(set.isEmpty());

        assertTrue(set.contains(56));
        assertTrue(set.contains(74));
        assertTrue(set.contains(32));
        assertTrue(set.contains(11));
        assertFalse(set.contains(22));

        set.remove(56);
        set.remove(22);

        assertFalse(set.contains(22));
        assertFalse(set.contains(56));
        assertTrue(set.contains(74));
        assertTrue(set.contains(32));
        assertTrue(set.contains(11));
    }

    @Test
    public void sizeTest() {
        ISet <Integer> set = new DoubleHashSet<Integer>(5);

        assertEquals(set.size(), 0);

        set.add(1);
        set.add(2);
        assertEquals(set.size(), 2);
        set.add(3);
        set.add(4);
        set.add(5);

        assertEquals(set.size(), 5);

        set.contains(5);
        set.remove(5);
        set.contains(5);
        assertEquals(set.size(), 4);
    }


    @Test
    public void isEmptyTest() {
        ISet <Integer> set = new DoubleHashSet<Integer>(5);

        assertTrue(set.isEmpty());
        set.add(1);
        set.add(2);
        assertFalse(set.isEmpty());
        set.remove(1);
        assertFalse(set.isEmpty());
        set.remove(2);
        assertTrue(set.isEmpty());
    }

    @Test
    public void collisionTest() {
        ISet <Integer> set = new DoubleHashSet<Integer>(5);

        assertFalse(set.contains(1));
        set.add(1);
        set.add(1);

        assertEquals(set.size(), 1);
        assertTrue(set.contains(1));

        set.remove(1);
        assertFalse(set.contains(1));
        assertEquals(set.size(), 0);
    }

    @Test
    public void noElementTest() {
        ISet <Integer> set = new DoubleHashSet<Integer>(5);

        assertFalse(set.contains(1));
        set.add(1);
        set.add(1);

        assertTrue(set.contains(1));

        set.remove(1);
        assertFalse(set.contains(1));
    }

    @Test
    public void removingNotExistingElement() {
        ISet <Integer> set = new DoubleHashSet<Integer>(5);
        set.remove(5);
        assertEquals(set.size(), 0);
    }

    @Test
    public void overflowTest() {
        ISet <Integer> set = new DoubleHashSet<Integer>(5);

        set.add(1);
        set.add(2);
        set.add(3);
        set.add(4);
        set.add(5);
        assertThrows(IllegalStateException.class, () -> set.add(6));
    }

    @Test
    public void emptySetTest() {
        ISet <Integer> set = new DoubleHashSet<Integer>(17);
        assertFalse(set.contains(56));
    }
}