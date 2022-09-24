package juc;

import java.util.concurrent.TimeUnit;

class HoldLockThread implements Runnable {

    private String lockA;
    private String lockB;

    public HoldLockThread(String lockA, String lockB) {
        this.lockA = lockA;
        this.lockB = lockB;
    }

    @Override
    public void run() {
        synchronized (lockA) {

            System.out.println(Thread.currentThread().getName() + "\t 自己持有: " + lockA + "\t 嘗試獲得" + lockB);
            try { TimeUnit.SECONDS.sleep(2); } catch (InterruptedException e) { e.printStackTrace(); }

            synchronized (lockB) {

                System.out.println(Thread.currentThread().getName() + "\t 自己持有: " + lockB + "\t 嘗試獲得" + lockA);
            }
        }
    }
}

public class DeadLockDemo {
    public static void main(String[] args) {
        String lockA = "lockA";
        String lockB = "lockB";
        new Thread(new HoldLockThread(lockA, lockB), "ThreadAAA").start();
        new Thread(new HoldLockThread(lockB, lockA), "ThreadBBB").start();

        // 死鎖排查方法

        /**
         * linux    ps -ef|grep xxxx    ls -l
         * window下的java運行程序也有類似ps的察看進程的命令，但是目前我們需要查看的只是java
         *          jps = java ps       jps -l
         * 得到PID
         * 使用jstack PID 查看進程狀況
         */
    }
}
