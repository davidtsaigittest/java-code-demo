package juc;


import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 1. 操作資源類(自身高內聚，自身帶有操作方法)
 */
class ShareData {
    private int number = 0;
    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    public void increment() throws Exception {
        lock.lock();
        try {
            //1. 判斷
            while (number != 0) {
                //等待，不能生產
                condition.await();
            }
            //2. 幹活
            number++;
            System.out.println(Thread.currentThread().getName() + "\t" + number);
            //3. 通知喚醒
            condition.signalAll();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void decrement() throws Exception {
        lock.lock();
        try {
            //1. 判斷
            while (number == 0) {
                //等待，不能生產
                condition.await();
            }
            //2. 幹活
            number--;
            System.out.println(Thread.currentThread().getName() + "\t" + number);
            //3. 通知喚醒
            condition.signalAll();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}

/**
 * 1. 高併發的情況，高內聚低耦合的前提下，以線程操作資源類
 * 2. 判斷幹活，喚醒通知
 * 3. 嚴防多線程併發下的虛假喚醒
 * <p>
 * 多線程的企業級模板口訣:
 * 1.   線程  操作(方法)  資源類
 * 2.   判斷  幹活  通知
 * 3.   防止虛假喚醒機制
 * 題目: 一個初始值為零的變量，兩個線程對其交替操作，一個+1一個-1，執行5輪
 */

public class ProdConsumerTraditionDemo {

    public static void main(String[] args) {
        ShareData shareData = new ShareData();

        new Thread(() -> {
            for (int i = 1; i <= 5; i++) {
                try {
                    shareData.increment();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "AA").start();

        new Thread(() -> {
            for (int i = 1; i <= 5; i++) {
                try {
                    shareData.decrement();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "BB").start();
    }
}
