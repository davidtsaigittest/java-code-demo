package juc;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * ThreadLocal
 *
 * 官方文檔說明:
 *  ThreadLocal提供線程局部變量，這些變量與正常變量不同，
 *  因為每個線程在訪問ThreadLocal實例的時候(通過期get或set方法)，都有自己的、獨立初始化的變量副本。
 *  ThreadLocal實例通常是類中的私有靜態字段，使用他的目的是希望將狀態(例如: 用戶ID或事務ID)與線程關聯起來
 *
 * 用途: 實現每個線程都有自己專屬的本地變量副本(各自線程使用自己的變量)，主要解決了讓每個線程綁定自己的值，通過使用get(),set()方法，
 * 獲取默認值或將其值改為當前線程所存的副本的值，從而避免了線程安全問題。與加鎖機制不同，使用另一種方式實現線程安全
 *
 * 使用注意:
 *  必須回收定義的ThreadLocal變量，尤其在線程池場景下，線程會被經常複用，若不清理自定義的ThreadLocal變量
 *  ，可能會影響後續業務邏輯和造成餒存洩漏問題，盡量在代理中使用try-finally進行回收
 *
 */

// 資源類
class House {
    int saleCount = 0;
    //賣出一棟總數 +1
    public synchronized void saleHouse() {
        ++saleCount;
    }
    /* Java 8 以前的寫法
    ThreadLocal<Integer> saleVolume = new ThreadLocal<Integer> () {
        @Override
        protected Integer initialValue() {
            return 0;
        }
    };*/
    ThreadLocal<Integer> saleVolume = ThreadLocal.withInitial(() -> 0);

    public void saleVolumeByThreadLocal() {
        saleVolume.set(1 + saleVolume.get());
    }

}

/**
 * 需求一: 5個業務賣房，主管只關心準確的賣出數量 => 總數加總使用synchronized
 * 需求二: 業務要抽成，因此需要知道每個業務分別的業績 => 使用ThreadLocal實現
 */
public class ThreadLocalDemo {
    public static void main(String[] args) {
        House house = new House();
//        newThread(house);
        threadPool(house);

    }

    /**
     * 線程池實作，線程複用
     * @param house
     */
    private static void threadPool(House house) {
        ExecutorService threadPool = Executors.newFixedThreadPool(3);
        try {
            for (int i = 0; i < 10; i++) {
                threadPool.submit(() -> {
                    try {
                        house.saleHouse();
                        Integer beforeSell = house.saleVolume.get();
                        house.saleVolumeByThreadLocal();
                        Integer afterSell = house.saleVolume.get();
                        System.out.println(Thread.currentThread().getName() + "\t beforeSell: " + beforeSell + "\t afterSell: " + afterSell);
                    } finally {
                        house.saleVolume.remove();
                    }
                });
            }
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            threadPool.shutdown();
        }

        try { TimeUnit.MILLISECONDS.sleep(300); } catch (InterruptedException e) { e.printStackTrace(); }

        System.out.println(Thread.currentThread().getName() + "\t 總共賣出幾棟房子: " + house.saleCount);
    }

    /**
     * new Thread()方式實作，線程不複用
     * @param house
     */
    private static void newThread(House house) {
        for (int i = 1 ; i <= 5; i++) {
            new Thread(() -> {
                int size = new Random().nextInt(5) + 1;
                try {
                    for (int j = 0; j <= size; j++) {
                        house.saleHouse();
                        house.saleVolumeByThreadLocal();
                    }
                    System.out.println(Thread.currentThread().getName() + "\t 號業務賣出: " + house.saleVolume.get());
                } finally {
                    house.saleVolume.remove();
                }
            }, String.valueOf(i)).start();
        }

        try { TimeUnit.MILLISECONDS.sleep(300); } catch (InterruptedException e) { e.printStackTrace(); }

        System.out.println(Thread.currentThread().getName() + "\t 總共賣出幾棟房子: " + house.saleCount);
    }
}
