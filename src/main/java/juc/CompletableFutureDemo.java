package juc;

import java.util.concurrent.*;

/**
 * Future功能增強版，減少阻塞和輪巡
 * 可傳入回調對象，當異步任務完成或發生異常，自動調用回調對象的回掉方法
 */
public class CompletableFutureDemo {

    private static void supplyAsync(ExecutorService threadPool) throws InterruptedException, ExecutionException {
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName());
            //暫停
            try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
            return "hello supplyAsync";
        }, threadPool);

        System.out.println(completableFuture.get());
    }

    private static void runAsync(ExecutorService threadPool) throws InterruptedException, ExecutionException {
        CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> {
            System.out.println(Thread.currentThread().getName());
            try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
        }, threadPool);

        System.out.println(completableFuture.get());
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
//        ExecutorService threadPool = Executors.newFixedThreadPool(3);
//         runAsync(threadPool); // 無返回值 runAsync()
//         supplyAsync(threadPool); // 有返回值supplyAsync()
//        threadPool.shutdown();
        whenComplete();

    }

    public static void whenComplete() {
        // 指定jdk線程池
        ExecutorService threadPool = Executors.newFixedThreadPool(3);

        try {
            CompletableFuture.supplyAsync(() -> {
                System.out.println(Thread.currentThread().getName() + "-----come in");
                int result = ThreadLocalRandom.current().nextInt(10);
                //暫停
                try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
                System.out.println("-----1秒後輸出結果" + result);
                return result;
            }, threadPool).whenComplete((v, e) -> {
                if (e == null) {
                    System.out.println("------計算完成，更新系統UpdateValue: " + v);
                }
            }).exceptionally(e -> {
                e.printStackTrace();
                System.out.println("異常情況" + e.getCause() + "\t" + e.getMessage());
                return null;
            });

            System.out.println(Thread.currentThread().getName() + "\t 線程先去忙其他任務");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            threadPool.shutdown();
        }

//        沒有指定線程池時，CompletableFuture默認使用的線程池(ForkJoinPool)會立刻關閉->這邊暫停3秒
//        try { TimeUnit.SECONDS.sleep(3); } catch (InterruptedException e) { e.printStackTrace(); }



    }
}
