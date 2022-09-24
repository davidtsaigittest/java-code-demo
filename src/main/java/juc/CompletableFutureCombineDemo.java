package juc;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * thenCombine
 * 對計算結果進行合併
 * 當兩個CompletionStage任務都完成後，最終能把兩個任務的結果一起交給thenCombine來處理
 * 1. 先完成的先等著，等待其他分支任務
 */
public class CompletableFutureCombineDemo {

    public static void main(String[] args) {
//        combineSimple();
        combineMultiple();
    }

    private static void combineSimple() {
    /*
    標準版 1+1 合併兩個
     */
        CompletableFuture<Integer> task1 = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + "\t ---啟動");
            try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
            return 10;
        });
        CompletableFuture<Integer> task2 = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + "\t ---啟動");
            try { TimeUnit.SECONDS.sleep(2); } catch (InterruptedException e) { e.printStackTrace(); }
            return 20;
        });
        CompletableFuture<Integer> result = task1.thenCombine(task2, (x, y) -> {
            System.out.println("---開始合併結果");
            return x + y;
        });
        System.out.println(result.join());
    }

    /**
     * 多次合併(練習)
     */
    private static void combineMultiple() {
        CompletableFuture<Integer> combineResult = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + "\t " + "---come in 1");
            return 10;
        }).thenCombine(CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + "\t " + "---come in 2");
            return 20;
        }), (x, y) -> {
            System.out.println(Thread.currentThread().getName() + "\t " + "---come in 3");
            return x + y;
        }).thenCombine(CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + "\t " + "---come in 4");
            return 30;
        }), (a, b) -> {
            System.out.println(Thread.currentThread().getName() + "\t " + "---come in 5");
            return a + b;
        });
        System.out.println("------線程結束, END");
        System.out.println(combineResult.join());
    }
}
