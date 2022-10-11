package lambda;

import lambda.demo.MyCalFun;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.function.Consumer;

/* Lambda表達式的基礎與法: Java8中引入一個新的"->"操作符，可拆分成兩部分:
 *
 * 左側: Lambda表達式的參數列表
 * 右側: Lambda表達式中所需執行的功能，即Lambda體
 *
 * 語法格式一: 無參數，無返回值
 *      () -> System.out.println("Hello Lambda!");
 *
 * 語法格式二: 有一個參數，無返回值
 *      (x) -> System.out.println(x);
 *
 * 語法格式三: 若只有一個參數，小括號可不寫
 *      x -> System.out.println(x);
 *
 * 語法格式四: 有兩個以上的參數，有返回值，並且Lambda體中有多條語句
*        Comparator<Integer> comparator = (x, y) -> {
 *           System.out.println("函數式街口");
 *           return Integer.compare(x, y);
 *       };
 *
 * 語法格式五: 若Lambda體中只有一條語句，return和大括號都可以省略不寫
 *      Comparator<Integer> comparator = (x, y) -> Integer.compare(x, y);
 *
 * 語法格式六: Lambda表達式的參數列表的數據類型可以省略不寫，因為JVM編譯器通過上下文推斷出，數據類型，即"類型推斷"
 *      (Integer x, Integer y) -> Integer.compare(x, y);
 * 上聯: 左右遇一括號省
 * 下聯: 左側推斷類型省
 * 橫批: 能省則省
 *
 * Lambda使用時需要"函數式接口"支持
 * 函數式接口: 接口中只有一個抽象方法的接口，稱為函數式接口，可以使用註解 @FunctionalInterface修飾，
 *              可以檢查是否是函數式接口
 */
public class TestLambda {

    @Test
    public void test1() {
        // jdk1.7以前，匿名內部類使用局部變量時必須聲明該變量是final，jdk1.8後可省略不寫
        int num = 0;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("Hello World!" + num);
            }
        };

        runnable.run();

        System.out.println("----------------------------");

        Runnable runnable1 = () -> System.out.println("Hello Lambda!");
        runnable1.run();
    }

    @Test
    public void test2() {
        Consumer consumer = (x) -> System.out.println(x);
        consumer.accept("我大尚硅谷");
    }
    @Test
    public void test3() {
        Comparator<Integer> comparator = (x, y) -> {
            System.out.println("函數式街口");
            return Integer.compare(x, y);
        };
    }
    @Test
    public void test4() {
        Comparator<Integer> comparator = (x, y) -> Integer.compare(x, y);
    }

    // 需求:對一個數進行運算
    @Test
    public void test5() {
        Integer num = operation(100, x -> x * x);
        System.out.println(num);
        System.out.println(operation(200, y -> y + 200));
    }

    public Integer operation(Integer num, MyCalFun mf) {
        return mf.getValue(num);
    }


}

