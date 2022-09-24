package juc;
/*
可重入(遞歸)鎖 - 同一個線程可以多次獲得同一把鎖，不需要等待鎖釋放，不會發生死鎖
一個線程中的多個流程可以獲取同一把鎖，持有這把同步鎖可以再次進入，自己可以獲取自己的內部鎖
1. 顯式ReentrantLock
2. 隱式synchronized
    - 同步代碼塊
    - 同步方法
 */
public class SynchronizedDemo {

    static Object objectLockA = new Object();

    public static void main(String[] args) {
        m1();
    }

    static void m1() {
        new Thread(() -> {
            synchronized (objectLockA) {
                System.out.println(Thread.currentThread().getName() + "\t --- 外層調用");
                synchronized (objectLockA) {
                    System.out.println(Thread.currentThread().getName() + "\t --- 中層調用");
                    synchronized (objectLockA) {
                        System.out.println(Thread.currentThread().getName() + "\t --- 內層調用");
                    }
                }
            }
        }, "t1").start();
    }

}
