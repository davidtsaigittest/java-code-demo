package lambda;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TestLambda1 {

    List<Employee> emps = Arrays.asList(
            new Employee("J3", 18, 9999.99),
            new Employee("L4", 38, 1111.11),
            new Employee("W5", 50, 6666.66),
            new Employee("J6", 16, 3333.33),
            new Employee("T7", 8, 7777.77)
    );

    @Test
    public void test1() {
        Collections.sort(emps, (e1, e2) -> {
            if(e1.getAge() == e2.getAge()) {
                return e1.getName().compareTo(e2.getName());
            } else {
                return -Integer.compare(e1.getAge(), e2.getAge());
            }
        });

        for(Employee emp : emps) {
            System.out.println(emp);
        }
    }

    @Test
    public void test2() {
        String trimStr = strHandler("\t\t\t 我大尚硅谷", str -> str.trim());
        System.out.println(trimStr);
        String upper = strHandler("abcdef", str -> str.toUpperCase());
        System.out.println(upper);
        String newStr = strHandler("我大尚硅谷威武", str -> str.substring(2, 5));
        System.out.println(newStr);
    }

    // 需求: 用於處理字符串
    public String strHandler(String str, MyFunction mf) {
        return mf.getValue(str);
    }

    @Test
    public void test3() {
        longOperation(100L, 200L, (x, y) -> x + y);
        longOperation(100L, 200L, (x, y) -> x * y);

    }

    // 需求: 對兩個Long型數據進行處理
    public void longOperation(Long lon1, Long long2, MyFunction2<Long, Long> mf) {
        System.out.println(mf.getValue(lon1, long2));
    }
}
