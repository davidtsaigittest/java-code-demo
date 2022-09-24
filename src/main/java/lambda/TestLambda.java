package lambda;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/*
 * Java8 內建四大核心函數式接口
 *
 * Consumer<T> : 消費型接口 -> 有去無回沒有回傳值
 *      void accept(T t);
 *
 * Supplier<T> : 供給型接口
 *      T get();
 *
 * Function<T, R> : 函數型接口
 *      R apply(T t);
 *
 * Predicate<T> : 斷言型接口
 *      boolean Test(T t);
 */
public class TestLambda {

    @Test
    void testConsumer() {
        depositMoney(10000.0, System.out::println );
    }

    void depositMoney(Double amt, Consumer<Double> consumer) {
        consumer.accept(amt);
    }

    @Test
    void testSupplier() {
        List<Integer> intList = getIntList(10, () -> (int) (Math.random()));
        for (Integer integer : intList) {
            System.out.println(integer);
        }
    }

    List<Integer> getIntList(int num, Supplier<Integer> supplier) {
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            list.add(supplier.get());
        }
        return list;
    }

}
