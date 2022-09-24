package juc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicStampedReference;


@NoArgsConstructor
@AllArgsConstructor
@Data
class Book {
    private int Integer;
    private String bookName;
}

public class ABADemo {

    static AtomicInteger atomicInteger = new AtomicInteger(100);

    static AtomicStampedReference stampedReference = new AtomicStampedReference(100, 1);

    public static void main(String[] args) {
//        abademo(); ABA問題演示，沒有使用版本號
//        singleThread(); 初階版本，單線程有使用版本號
        multiThread(); // 多線程使用版本號 => 可解決ABA問題
    }

    /**
     * T3線程發生ABA問題
     * 期望結果:
     * T4修改前先取得並保存首次版本號stamp，等待1秒後，首次版本號被T3修改了，T4會檢查
     * 1. reference值 => 100通過
     * 2. stamp => 不是1，不通過
     * 目前版本號與stamp不同
     * 導致修改失敗 => 返回boolean = false
     *
     * 結論 => 使用AtomicStampedReference的戳記stamp特性，可以解決ABA問題
     *
     */
    private static void multiThread() {

        new Thread(() -> {
            int stamp = stampedReference.getStamp();
            System.out.println(Thread.currentThread().getName() + "\t首次版本號:" + stamp);

            //暫停500毫秒線程，保證後面t4線程初始化拿到的版本號和我一樣
            try { TimeUnit.MILLISECONDS.sleep(500); } catch (InterruptedException e) { e.printStackTrace(); }

            // 第一次改動 100 改成 101
            stampedReference.compareAndSet(100, 101, stamp, stampedReference.getStamp() + 1);
            System.out.println(Thread.currentThread().getName() + "\t 2次流水號:" + stampedReference.getStamp());

            // 第二次改動 101 改回 100
            stampedReference.compareAndSet(101, 100, stampedReference.getStamp(), stampedReference.getStamp() + 1);
            System.out.println(Thread.currentThread().getName() + "\t 3次流水號:" + stampedReference.getStamp());
        }, "t3").start();

        new Thread(() -> {
            int stamp = stampedReference.getStamp();
            System.out.println(Thread.currentThread().getName() + "\t首次版本號:" + stamp);

            //暫停1秒線程，等待上面t3線程發生ABA問題
            try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }

            boolean b = stampedReference.compareAndSet(100, 2022, stamp, stamp + 1);

            System.out.println(b + "\t" + stampedReference.getReference() + "\t" + stampedReference.getStamp());
        }, "t4").start();
    }

    private static void abademo() {
        new Thread(() -> {
            atomicInteger.compareAndSet(100, 101);
            try { TimeUnit.MILLISECONDS.sleep(10); } catch (InterruptedException e) { e.printStackTrace(); }
            atomicInteger.compareAndSet(101, 100);
        }, "t1").start();

        new Thread(() -> {
            try { TimeUnit.MILLISECONDS.sleep(200); } catch (InterruptedException e) { e.printStackTrace(); }
            System.out.println(atomicInteger.compareAndSet(100, 2022) + "\t" + atomicInteger.get());
        }, "t2").start();
    }

    /**
     * AtomicStampedReference 單一線程演示
     */
    private static void singleThread() {
        Book javaBook = new Book(1, "javaBook");
        AtomicStampedReference<Book> stampedReference = new AtomicStampedReference<>(javaBook, 1);

        System.out.println(stampedReference.getReference() + "\t " + stampedReference.getStamp());

        Book mysqlBook = new Book(1, "mysqlBook");

        boolean b = stampedReference.compareAndSet(javaBook, mysqlBook, stampedReference.getStamp(), stampedReference.getStamp() + 1);

        System.out.println(b + "\t" + stampedReference.getReference() + "\t" + stampedReference.getStamp());

        b = stampedReference.compareAndSet(mysqlBook, javaBook, stampedReference.getStamp(), stampedReference.getStamp() + 1);

        System.out.println(b + "\t" + stampedReference.getReference() + "\t" + stampedReference.getStamp());
    }


}
