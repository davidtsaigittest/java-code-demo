package jvm;

import java.util.concurrent.TimeUnit;

class MyNumber {
    volatile int number = 10;
    public void addTo1205() {
        this.number = 1205;
    }
}
/**
 * JMM Java內存模型
 * CPU>內存>硬盤
 * CPU與內存間有緩存
 * JVM在內存運行
 * JMM是一抽象規範，定義程序中各個變量的訪問方式
 */
public class JMMNote {

    public static void main(String[] args) {
        MyNumber myNumber = new MyNumber();
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "\t *********** come in");
            try { TimeUnit.SECONDS.sleep(3); } catch (InterruptedException e) { e.printStackTrace(); }
            myNumber.addTo1205();
            System.out.println(Thread.currentThread().getName() + "\t update number, number value: " + myNumber.number);
        }, "AA").start();

        while (myNumber.number == 10) {
            // 需要有一種通知機制告訴main線程，number已經被修改為1205，跳出while -> 使用volatile可見性
        }

        System.out.println(Thread.currentThread().getName() + "\t mission is over");
    }
}
