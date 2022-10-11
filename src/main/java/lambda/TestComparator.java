package lambda;

import lombok.*;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.TreeSet;

public class TestComparator {
    // 匿名內部類
    @Test
    public void test1() {
        Comparator<Integer> comparator = new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return Integer.compare(o1, o2);
            }
        };
        TreeSet<Integer> ts = new TreeSet<>(comparator);
    }

    // Lambda 表達式
    @Test
    public void testLambda() {
        Comparator<Integer> com = (o1, o2) -> Integer.compare(o1, o2);
        TreeSet<Integer> ts = new TreeSet<>(com);
    }

}
