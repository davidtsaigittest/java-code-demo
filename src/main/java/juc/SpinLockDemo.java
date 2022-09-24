package juc;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
/*
自旋鎖(spinlock)
是指嘗試獲取鎖的線程不會立即阻塞，而是採用循環的方去嘗試獲取鎖
舉例:CAS Unsafe.getAddInt()
* 優點: 減少線程上下文切換的消耗，循環比較獲取直到成功為止，沒有類似wait的阻塞
       避開synchronized這種重量級的加鎖操作，底層調用Unsafe類，實現了輕量級的加鎖方式，避開了阻塞
* 缺點:
        1. 循環會消耗CPU資源，若循環時間長會拖慢系統(類似死循環CPU空轉)
        2. ABA問題(一句話: 因為只看開始狀態和結果狀態，所以過程被人動過改過無法察覺)
            解法: 修改資料時同時加入版本號(戳記) => 只要被改過，版本號就會+1
            實現: 用AtomicStampedReference(帶有戳記的原子引用)
題目:手寫一個自旋鎖
通過CAS操作完成自旋鎖，A線程先進來調用lock方法自己持有鎖5秒鐘，B隨後進來後發現
當前有限程持有鎖，不是null，所以只能通過自旋等待，直到A釋放鎖後B隨後搶到。
 */
public class SpinLockDemo {

    //原子引用線程
    AtomicReference atomicReference = new AtomicReference<Thread>();

    public void lock() {
        Thread thread = Thread.currentThread();
        System.out.println(Thread.currentThread().getName() + "\t come in o(n_n)o");
        // 若當前原子引用是空的，就將引用設為當前線程，若原子引用不是空的，就會持續自旋(while不會結束) => 反覆循環比較
        // 第一次CAS成功後不會進while
        while(!atomicReference.compareAndSet(null, thread)) { //為避免CAS成功返回true後發生死循環，要!取反
        }
    }

    public void unlock() {
        Thread thread = Thread.currentThread();
        // 解鎖，原先指向自己的原子引用，改為指向null，給下一個thread使用
        atomicReference.compareAndSet(thread, null);
        System.out.println(Thread.currentThread().getName() + "\t invoked unlock");
    }

    public static void main(String[] args) {

        SpinLockDemo spinLockDemo = new SpinLockDemo();

        new Thread(() -> {
            spinLockDemo.lock();
            // AA 持有鎖5秒後才解鎖
            try { TimeUnit.SECONDS.sleep(5); } catch (InterruptedException e) { e.printStackTrace(); }
            spinLockDemo.unlock();
        }, "AA").start();

        // 暫停1秒再啟動BB
        try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }

        new Thread(() -> {
            spinLockDemo.lock();
            try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
            spinLockDemo.unlock();
        }, "BB").start();
    }

}
