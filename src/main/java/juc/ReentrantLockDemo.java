package juc;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Phone implements Runnable {
    public synchronized void sendSMS() {
        System.out.println(Thread.currentThread().getName() + "\t invoke sendSMS()");
        sendEmail();
    }

    public synchronized void sendEmail() {
        System.out.println(Thread.currentThread().getName() + "\t #####invoked sendEmail()#####");
    }

    Lock lock = new ReentrantLock();

    @Override
    public void run() {
        get();
    }

    public void get() {
        lock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + "\t invoke get()");
            set();
        } finally {
            lock.unlock();
        }
    }

    public void set() {
        lock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + "\t invoke set()");
        } finally {
            lock.unlock();
        }
    }
}

/**
 * 可重入鎖(也可叫遞歸鎖)
 * 1. 指的是同一個線程外層獲得鎖之後，內層遞歸函數仍然能獲取該鎖的代碼
 *  在同一個線程在外層方法獲取鎖的時候，在進入內層方法會自動獲取鎖

 *  也就是說，線程可以進入任何一個他已經擁有的鎖，所同步著的代碼塊
 *
 * 2. 可重入鎖最大的作用是避免死鎖
 */
public class ReentrantLockDemo {
    static Lock lock = new ReentrantLock();

    public static void main(String[] args) {
//        lock();
        Phone phone = new Phone();

        new Thread(() -> {
            phone.sendSMS();
        }, "t1").start();

        new Thread(() -> {
            phone.sendEmail();
        }, "t2").start();

        try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }

        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();


        Thread t3 = new Thread(phone, "t3");
        Thread t4 = new Thread(phone, "t4");
        t3.start();
        t4.start();
    }

    private static void lock() {
        new Thread(() -> {
            lock.lock();
            lock.lock();
            try {
                System.out.println("---外層");
                lock.lock();
                try {
                    System.out.println("---內層");
                } finally {
                    lock.unlock();
                }
            } finally {
                lock.unlock();
                lock.unlock();
            }
        }, "t1").start();
    }
}
