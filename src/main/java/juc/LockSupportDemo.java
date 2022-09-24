package juc;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 要求t1現成等待3秒鐘，3秒後t2喚醒t1繼續工作
 * <p>
 * 1. 異常情況wait(), notify() 方法不放在同步代碼塊內
 * => 拋IllegalMonitorStateException
 * 2. 先notify後wait
 * 3. 結論
 * Object類中的wait, notify, notifyAll用於線程等待和喚醒的方法，都必須在synchronized內部執行(必須用到關鍵字synchronized)
 * 必須先wait後notify否則線程無法被喚醒
 *
 *
 * 2.1 Condition的await, signal需要與Lock的lock, unlock同時使用否則也是會拋錯
 * =>拋IllegalMonitorStateException
 * 2.2 要先執行await再執行signal才能成功喚醒線程
 *
 *
 */
public class LockSupportDemo {

    static Object objectLock = new Object();
    static Lock lock = new ReentrantLock();
    static Condition condition = lock.newCondition();

    public static void main(String[] args) {
//        synchronziedWaitNotify();
//        lockAwaitSignal();
        lockSupport();
    }

    /**
     * 1. LockSupport 支持先喚醒後等待
     * 2. LockSupport 無鎖塊要求，park(), unpark()可單獨使用
     */
    private static void lockSupport() {
        Thread a = new Thread(() -> {
//            try { TimeUnit.SECONDS.sleep(3); } catch (InterruptedException e) { e.printStackTrace(); }
            // park()可再unpark()前執行
            System.out.println(Thread.currentThread().getName() + "\t" + "------come in");
            LockSupport.park(); // 調用park()被阻塞...等待通知等待放行，需要許可證(permit)
            System.out.println(Thread.currentThread().getName() + "\t" + "------被喚醒");
        }, "A");
        a.start();

        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "\t" + "------通知了");
            LockSupport.unpark(a); // 調用park()被阻塞...等待通知等待放行，需要許可證(permit)
        }, "B").start();
    }

    private static void lockAwaitSignal() {
        new Thread(() -> {
            //try { TimeUnit.SECONDS.sleep(3); } catch (InterruptedException e) { e.printStackTrace(); }
            // 若讓A線程await睡3秒，使B線程signal早於await先執行，則A線程將無法被喚醒
            lock.lock();
            try {
                try {
                    System.out.println(Thread.currentThread().getName() + "\t" + "------come in");
                    condition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + "\t" + "------被喚醒");
            } finally {
                lock.unlock();
            }
        }, "A").start();

        new Thread(() -> {
//            lock.lock();
            try {
               condition.signal();
                System.out.println(Thread.currentThread().getName() + "\t" + "------通知");
            } finally {
//                lock.unlock();
            }
        }, "B").start();
    }

    private static void synchronziedWaitNotify() {
        new Thread(() -> {
//            synchronized (objectLock) {
                System.out.println(Thread.currentThread().getName() + "\t" + "------come in");
                try {
                    objectLock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + "\t" + "------被喚醒");
//            }
        }, "A").start();
        new Thread(() -> {
//            synchronized (objectLock) {
                objectLock.notify();
                System.out.println(Thread.currentThread().getName() + "\t" + "------通知");
//            }
        }, "B").start();
    }
}
