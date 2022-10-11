package lambda;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Java8 內建四大核心函數式接口
 *
 *   Consumer<T> : 消費型接口 -> 有去無回沒有回傳值
 *        void accept(T t);
 *
 *   Supplier<T> : 供給型接口
 *        T get();
 *
 *   Function<T, R> : 函數型接口
 *        R apply(T t);
 *
 *   Predicate<T> : 斷言型接口
 *        boolean Test(T t);
 */
public class TestLambda3 {

    // Predicate<T> 斷言型接口:
    @Test
    public void test4() {
        List<String> list = Arrays.asList("Hello", "atguigu", "Lambda", "www", "ok");
        List<String> strList = filterStr(list, s -> s.length() > 3);
        for (String str : strList) {
            System.out.println(str);
        }
    }

    // 需求: 將滿足條件的字符串，放入集合中
    public List<String> filterStr(List<String> list, Predicate<String> predicate) {
        List<String> strList = new ArrayList<>();
        for (String str : list) {
            if(predicate.test(str)) {
                strList.add(str);
            }
        }
        return strList;
    }

    //Function<T, R>函數型接口:
    public void test3() {
        String newStr = strHandler("\t\t\t 我大尚硅谷威武", str -> str.trim());
        System.out.println(newStr);

        String subStr = strHandler("我大尚硅谷威武", str -> str.substring(2, 5));
        System.out.println(subStr);
    }

    //需求: 用於處理自符串
    public String strHandler(String str, Function<String, String> funciton) {
        return funciton.apply(str);
    }

    // Supplier<T> 供給型接口:
    @Test
    public void test2(){
        List<Integer> numList = getNumList(20, () -> (int) (Math.random() * 100));
        for (Integer num : numList) {
            System.out.println(num);
        }
    }
    //需求: 產生指定個數的整數，並放入集合中
    public List<Integer> getNumList(int num, Supplier<Integer> supplier) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            Integer n = supplier.get();
            list.add(n);
        }
        return list;
    }


    //Consumer<T> 消費型接口:
    @Test
    public void test1() {
        happy(10000, m -> System.out.println("消費: " + m + "元"));
    }

    public void happy(double money, Consumer<Double> consumer) {
        consumer.accept(money);
    }

    @Test
    void testConsumer() {
        depositMoney(10000.0, System.out::println );
    }

    void depositMoney(Double amt, Consumer<Double> consumer) {
        consumer.accept(amt);
    }

    List<Integer> getIntList(int num, Supplier<Integer> supplier) {
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            list.add(supplier.get());
        }
        return list;
    }

    @Test
    void testSupplier() {
        List<Integer> intList = getIntList(10, () -> (int) (Math.random()));
        for (Integer integer : intList) {
            System.out.println(integer);
        }
    }

}
