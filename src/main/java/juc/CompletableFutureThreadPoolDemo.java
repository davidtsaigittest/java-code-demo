package juc;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 1. 沒有傳入自訂線程池，都默認使用ForkJoinPool
 * 2. 傳入自訂線程池
 *      若執行第一個任務傳入自訂線程池，調用thenRun執行第二個任務時，第二個任務會和第一個任務使用是共用同一個線程池
 *      若在後續任務調用thenRunAsync, thenAcceptAsync等Async方法，即使第一個任務有指定自訂線程池，後續線程還是會使用ForkJoinPool
 * 3. 線程若處理太快，系統優化切換原則，會直接使用main線程處理
 */
public class CompletableFutureThreadPoolDemo {

    public static void main(String[] args) {
        ExecutorService threadPool = Executors.newFixedThreadPool(5);
        try {
            CompletableFuture<Void> completableFuture = CompletableFuture.supplyAsync(() -> {
                try { TimeUnit.MILLISECONDS.sleep(20); } catch (InterruptedException e) { e.printStackTrace(); }
                System.out.println("1號任務: " + Thread.currentThread().getName());
                return "abcd";
            }, threadPool).thenRun(() -> {
                try { TimeUnit.MILLISECONDS.sleep(20); } catch (InterruptedException e) { e.printStackTrace(); }
                System.out.println("2號任務: " + Thread.currentThread().getName());
            }).thenRunAsync(() -> {
                try { TimeUnit.MILLISECONDS.sleep(10); } catch (InterruptedException e) { e.printStackTrace(); }
                System.out.println("3號任務: " + Thread.currentThread().getName());
            }).thenRun(() -> {
                try { TimeUnit.MILLISECONDS.sleep(10); } catch (InterruptedException e) { e.printStackTrace(); }
                System.out.println("4號任務: " + Thread.currentThread().getName());
            });
            completableFuture.get(2L, TimeUnit.SECONDS);
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            threadPool.shutdown();
        }
    }

}
