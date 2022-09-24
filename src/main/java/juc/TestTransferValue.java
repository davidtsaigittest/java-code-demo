package juc;

import java.util.concurrent.TimeUnit;

public class TestTransferValue {

    public void changeValue(int age) {
        age = 30;
    }

    public static void main(String[] args) {
        TestTransferValue test = new TestTransferValue();
        int age = 20;
        test.changeValue(age);
        System.out.println("age -----" + test);
    }

}
