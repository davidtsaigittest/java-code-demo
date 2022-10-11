package lambda.demo;

import lambda.Employee;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class TestFilter {

    List<Employee> employees = Arrays.asList(
            new Employee("J3", 18, 9999.99),
            new Employee("L4", 38, 1111.11),
            new Employee("W5", 50, 6666.66),
            new Employee("J6", 16, 3333.33),
            new Employee("T7", 8, 7777.77)
    );

    // 需求:獲取當前公司中員工年齡大於35的員工信息
    @Test
    public void testFilter() {
        List<Employee> list = filterEmployees(employees);
        for (Employee employee : list) {
            System.out.println(employee);
        }
    }
    public List<Employee> filterEmployees(List<Employee> list) {
        ArrayList<Employee> emps = new ArrayList<>();
        for (Employee emp : list) {
            if(emp.getAge() >= 35) {
                emps.add(emp);
            }
        }
        return emps;
    }

    // 優化方式一: 策略設計模式
    @Test
    public void testFilter1() {
        List<Employee> list = filterEmployee(employees, new FilterEmployeeByAge());
        for (Employee employee : list) {
            System.out.println(employee);
        }
        System.out.println("--------------------");
        //新需求:獲取當前公司員工工資大於5000的員工信息
        List<Employee> list1 = filterEmployee(list, new FilterEmployeeBySalary());
        for (Employee employee : list1) {
            System.out.println(employee);
        }
    }

    public List<Employee> filterEmployee(List<Employee> list, MyPredicate<Employee> mp) {
        List<Employee> emps = new ArrayList<>();
        for (Employee employee : list) {
            if (mp.test(employee)) {
                emps.add(employee);
            }
        }
        return emps;
    }

    // 優化方式二: 匿名內部類
    @Test
    public void testFilter2() {
        List<Employee> list = filterEmployee(employees, new MyPredicate<Employee>() {
            @Override
            public boolean test(Employee employee) {
                return employee.getSalary() >= 5000;
            }
        });
        for (Employee employee : list) {
            System.out.println(employee);
        }
    }

    // 優化方式三: Lambda表達式
    @Test
    public void testFilter3() {
        filterEmployee(employees, e -> e.getSalary() >= 5000).forEach(System.out::println);
    }

    // 優化方式四: Lambda + Stream + 方法引用
    @Test
    public void testFilter4() {
        employees.stream()
                .filter(e -> e.getSalary() >= 5000)
                .limit(2)
                .forEach(System.out::println);

        System.out.println("---------------------------");

        employees.stream()
                .map(Employee::getName)
                .forEach(System.out::println);
    }
}

