package lambda;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class TestStreamAPI {

    List<Person> personList = Arrays.asList(
            new Person(18),
            new Person(19),
            new Person(20),
            new Person(21),
            new Person(22)
    );

    List<String> strings = Arrays.asList(
            "aaa",
            "bbb",
            "ccc"
    );

    // 創建Stream
    @Test
    void test() {
        // 1. Collection集合下的stream() or parallelStream()
        ArrayList<String> list = new ArrayList<>();
        list.stream();
        // 2. Arrays的static方法stream()
        Arrays.stream(new int[] {});
        // 3. Stream類的static of()
        Stream.of(1, 2, 3);
        // 4. Stream iterate()迭代 & generate()生成無限流
        Stream.iterate(0, (num) -> num + 1);
        Stream.generate(Math::random);
    }

    // 映射
    @Test
    void testMap() {
        personList.stream().map(p-> p.getAge()).forEach(System.out::println);
    }

    @Test
    void testFlatMap() {
        strings.stream().flatMap(TestStreamAPI::getCharacterStream).forEach(System.out::println);
    }

    private static Stream<Character> getCharacterStream(String str) {
        List<Character> list = new ArrayList<>();
        for (char c : str.toCharArray()) {
            list.add(c);
        }
        return list.stream();
    }


    // map-reduce
    @Test
    void reduceMap () {
        personList.stream()
                .map(person -> 1)
                .reduce(0, (x, y) -> Integer.sum(x, y));
    }
}
