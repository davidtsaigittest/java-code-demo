package juc;

import java.util.concurrent.*;

/**
 * 對計算結果進行處理thenApply, handle
 *
 * CompletableFuture實現CompletableFuture, CompletableStage接口
 * CompletableStage
 * 代表異步計算的某個過程，一個階段(stage)完成後可能會觸發下一個階段
 * 一個stage的執行可能是被單個stage的完成觸發，也可能是由多個stage一起觸發
 * 類似linux的管道分隔符|傳參
 */
public class CompletableFutureAPI2Demo {
    public static void main(String[] args) {

        ExecutorService threadPool = Executors.newFixedThreadPool(3);
//        thenApply(threadPool);
        handle(threadPool);
        threadPool.shutdown();
    }

    /**
     * 計算存在依賴關係(當前出錯，不走下一步)，有異常就叫停
      * @param threadPool
     */
    private static void thenApply(ExecutorService threadPool) {

        System.out.println("-------start thenApply-------");
        System.out.println("");
        System.out.println("");

        CompletableFuture.supplyAsync(() -> {
            try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
            System.out.println("111");
            return 1;
        }, threadPool).thenApply(f -> {
            System.out.println("222");
            int i = 10/0;
            return f + 2;
        }).thenApply(f -> {
            System.out.println("333");
            return f + 3;
        }).whenComplete((v, e) -> {
            if (e == null) {
                System.out.println("-----計算結果: " + v);
            }
        }).exceptionally(e -> {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return null;
        });

        System.out.println("------主線程先去忙其他任務");
    }

    /**
     * handle有異常也可以繼續，根據帶的異常參數可以進一步處理
     *
     * handle + whenComplete 類似try/finally
     * exceptionally類似try/catch
     *
     * @param threadPool
     */
    private static void handle(ExecutorService threadPool) {

        System.out.println("-------start handle-------");
        System.out.println("");
        System.out.println("");

        CompletableFuture.supplyAsync(() -> {
            try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
            System.out.println("------111");
            return 1;
        }, threadPool).handle((f, e) -> {
            System.out.println("------222");
            int i = 10/0;
            return f + 2;
        }).handle((f, e) -> {
            System.out.println("------333");
            return f + 3;
        }).whenComplete((v, e) -> {
            System.out.println("------執行whenComplete");
            if (e == null) {
                System.out.println("-----計算結果: " + v);
            }
        }).exceptionally(e -> {
            e.printStackTrace();
            System.out.println("------執行exceptionally: " + e.getMessage());
            return null;
        });
        System.out.println("------主線程先去忙其他任務");
    }
}
