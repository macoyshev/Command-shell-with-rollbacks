import com.company.DoubleHashSet;
import com.company.ISet;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class DoubleHashSetTestRandomTests {
    int testsAmount = 1000000000;
    int setCapacity = 11;
    Set<Integer> model = new HashSet<>();
    ISet<Integer> set  = new DoubleHashSet<>(setCapacity);
    Integer[] arr = {0,1,2,3,4,5,6,7,8,9};
    List<Integer> range = Arrays.asList(arr);

    @Test
    public void Test_RandomInputs() {
        for(int i = 0; i < testsAmount; i++) {
            Integer random = randomInteger();
            OpType opType = OpType.getRandom();

            switch(opType) {
                case ADD:
                    if(model.size() == 11) {
                        assertThrows(IllegalStateException.class,
                                () -> set.add(random));
                    } else {
                        model.add(random);
                        set.add(random);
                    }
                    break;
                case SIZE:
                    if (model.size() != set.size())
                        System.out.println(set.size());
                    break;
                case EMPTY:
                    assertEquals(model.isEmpty(), set.isEmpty());
                    break;
                case REMOVE:
                    model.remove(random);
                    set.remove(random);
                    break;
                case CONTAINS:
                    assertEquals(model.contains(random), set.contains(random));
            }
        }
    }

    private int randomInteger() {
        return range.get(new Random().nextInt(10));
    }

    private enum OpType {
        ADD, REMOVE, CONTAINS, SIZE, EMPTY;

        public static OpType getRandom() {
            int x = new Random().nextInt(OpType.class.getEnumConstants().length);
            return OpType.class.getEnumConstants()[x];
        }
    }
}