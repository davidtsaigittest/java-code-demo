package lambda;

import org.junit.jupiter.api.Test;

import java.util.Optional;

public class TestOptional {

    @Test
    void testOptional() {
        Optional<Person> op = Optional.ofNullable(new Person());

        Optional<Integer> age = op.map(e -> e.getAge());
        System.out.println(age.get());

//        Optional<Person> person = op.flatMap(Optional::of);

        Optional<String> op2 = op.flatMap(e -> Optional.of(e.getName()));

        System.out.println(op2.get());

    }

}
