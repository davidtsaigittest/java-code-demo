package juc;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 面試題目: synchronized和Lock有什麼區別?用Lock有什麼好處?你舉例說說
 * 1 原始構成
 * synchronized關鍵字屬於JVM層面,
 *  monitorenter(底層是通過monitor對象來完成，其實wait/notify等發法也依賴於monitor對象，只有再同步塊或方法中才能調用wait/notify方法
 *  monitorexit
 * lock是具體類(java.util.concurrent.locks.lock)是API層面的鎖
 *
 * 2 使用方法
 *  synchronized 不需要用戶手動去釋放鎖，當synchronized代碼執行完後洗統會自動讓線程釋放對鎖的佔用
 *  ReentrantLock 需要用戶手動釋放鎖，若沒有主動釋放鎖，就有可能出現死鎖現象。
 *  需要lock()和unlock()方法配合try/finally語句塊來完成
 *
 * 3 等待是否可中斷
 *  synchronized 不可中斷，除非拋出異常或者正常運行完成
 *  ReentrantLock 可中斷， 1. 設置超時方法 tryLock(long timeout, TimeUnit unit)
 *                       2. lockInterruptibly()放代碼塊中，底層會調用interrupt()方法中斷
 *
 * 4 加鎖是否公平
 *  * 公平鎖: 是指多個線程按照申請鎖的順序來獲取鎖，類似排隊
 *  * 非公平鎖: 是指多個現成獲取鎖的順序並不是按照申請鎖的順序，有可能後申請線程比先申請線程優先獲取鎖
 *            在高併發下，可能造成優先級反轉或飢餓現象
 *  ReentrantLock而言，吞吐量非公平鎖 > 公平鎖
 *
 *    synchronized非公平鎖
 *    ReentrantLock兩者都可，默認非公平鎖，透過構造器傳入boolean，true為公平鎖，false為非公平鎖
 *
 *
 * 5 鎖綁定多個條件Condition
 *  synchronized沒有
 *  ReentrantLock用來實現分組喚醒，可以精確喚醒需要被喚醒的線程們，而不是像synchronized碎機喚醒一個，或者一次喚醒全部線程。
 *
 *
 * 題目: 多線程之間按順序調用，實現A->B->C三個線程啟動，要求如下:
 * A打印5次，B打印10次，C打印15次
 * 循環10輪
 */
class ShareResource {
    private int number = 1; //A:1 B:2 C:3
    private Lock lock = new ReentrantLock();
    Condition c1 = lock.newCondition();
    Condition c2 = lock.newCondition();
    Condition c3 = lock.newCondition();

    /**
     * A打印5次
     */
    public void print5() {
        lock.lock();
        try {
            //1. 判斷
            while(number != 1) {
                c1.await();
            }
            //2. 幹活
            for (int i = 1; i <= 5; i++) {
                System.out.println(Thread.currentThread().getName() + "\t" + i);
            }
            //3. 通知
            number = 2;
            c2.signal();
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
    /**
     * B打印10次
     */
    public void print10() {
        lock.lock();
        try {
            //1. 判斷
            while(number != 2) {
                c2.await();
            }
            //2. 幹活
            for (int i = 1; i <= 10; i++) {
                System.out.println(Thread.currentThread().getName() + "\t" + i);
            }
            //3. 通知
            number = 3;
            c3.signal();
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    /**
     * C打印15次
     */
    public void print15() {
        lock.lock();
        try {
            //1. 判斷
            while(number != 3) {
                c3.await();
            }
            //2. 幹活
            for (int i = 1; i <= 15; i++) {
                System.out.println(Thread.currentThread().getName() + "\t" + i);
            }
            //3. 通知
            number = 1;
            c1.signal();
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}

public class SynchronizedAndReentrantLockDemo {

    public static void main(String[] args) {

        ShareResource shareResource = new ShareResource();

        new Thread(() -> {
            for (int i = 1; i < 10; i++) {
                shareResource.print5();
            }
        }, "AA").start();

        new Thread(() -> {
            for (int i = 1; i < 10; i++) {
                shareResource.print10();
            }
        }, "BB").start();

        new Thread(() -> {
            for (int i = 1; i < 10; i++) {
                shareResource.print15();
            }
        }, "CC").start();
    }

}
