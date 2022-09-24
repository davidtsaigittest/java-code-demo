package lambda;

import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.function.*;

/*
    方法引用:
        抽象方法參數列表,返回的類型要與實例方法的返回類型相同
    
        條件一 : lambda中方法體已經被實作完畢就可以使用方法引用寫法
        條件二 : 被引用方法的參數列表和返回值類型，要與抽象方法的參數列表與返回值相同
    
        若 Lambda 參數列表中的第一參數式實例方法的調用者，且第二參數事實理方法的參數時
        -> 可以使用ClassName :: method
    
        三種語法格式 :
    
        對象::實例方法名
    
        類::靜態方法名
    
        類::實例方法名
        
    構造器引用:
        ClassName::new
 */
public class TestMethodRef {

    @Test
    void test6() {
        Function<Integer, Person> function = (x) -> new Person(x);
//        Function<Integer, Person> function1 = Person::new;
        BiFunction<String, Integer, Person> function1 = Person::new;
        Person person = function1.apply("David", 28);
        System.out.println(person);
    }

    @Test
    void test5() {
        Supplier<Person> sup = () -> new Person();
        Supplier<Person> sup1 = Person::new;
    }
    
    @Test
    void test() {
        Consumer<String> consumer1 = (x) -> System.out.println(x);
        Consumer<String> consumer = System.out::println;
        consumer.accept("abcdef");
    }

    @Test
    void test1() {
        Person person = new Person();
        Supplier<Person> supplier2 = Person::person;
        Supplier<Integer> supplier1 = person::getAge;
        Supplier<String> supplier = () -> person.getName();


        supplier.get();
    }

    @Test
    void test2() {
        Supplier<Person> supplier2 = Person::person;

        Comparator<Integer> comparator = (x, y) -> Integer.compare(x, y);

        Comparator<Integer> comparator1 = Integer::compare;
        comparator1.compare(1, 2);
    }

    @Test
    void test3() {
        BiPredicate<String, String> bp = (x, y) -> x.equals(y);
        BiPredicate<String, String> bp1 = String::equals;
    }

}
