package juc.atomic;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicMarkableReference;

/**
 * AtomicStampedReference:
 *  1. 帶有版本號的飲用類型原子類，可以解決ABA問題
 *  2. 解決修改過幾次
 * AtomicMarkableReference:
 *  1. 原子更新帶有標記位的引用類型對象
 *  2. 解決是否修改過: 將狀態戳記簡化為true/false，一次性使用
 *
 */
public class AtomicMarkableReferenceDemo {

    static AtomicMarkableReference markableReference = new AtomicMarkableReference(100, false);
    public static void main(String[] args) {
        new Thread(() -> {
            boolean marked = markableReference.isMarked();
            System.out.println(Thread.currentThread().getName() + "\t 默認標示: " + marked);
            // 暫停1秒鐘線程，等待後面的T2線程和我拿到一樣的flag，都是false
            try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
            boolean b = markableReference.compareAndSet(100, 1000, marked, !marked);
            System.out.println(Thread.currentThread().getName() + "\t t1線程CAS result: " + b);
        }, "t1").start();

        new Thread(() -> {
            boolean marked = markableReference.isMarked();
            System.out.println(Thread.currentThread().getName() + "\t 默認標示: " + marked);
            // t2等2秒後也要修改 => 預期修改失敗，因為flag已經不是預設值false
            try { TimeUnit.SECONDS.sleep(2); } catch (InterruptedException e) { e.printStackTrace(); }
            boolean b = markableReference.compareAndSet(100, 1000, marked, !marked);
            System.out.println(Thread.currentThread().getName() + "\t t2線程CAS result: " + b);
            System.out.println(Thread.currentThread().getName() + "\t " + markableReference.isMarked());
            System.out.println(Thread.currentThread().getName() + "\t " + markableReference.getReference());
        }, "t2").start();
    }
}
