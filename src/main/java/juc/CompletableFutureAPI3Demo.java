package juc;

import java.util.concurrent.CompletableFuture;

/**
 * 對計算結果進行消費
 * thenAccept接受處理結果，並消費處理，無返回結果
 *
 * 比較:
 * thenRun(Runnable runnable): 任務A執行完執行B，並且B不需要A的結果，A沒有返回值
 *
 * thenAccept(Consumer action): 任務A執行完執行B，B需要A的結果，但B '沒有返回值'
 *
 * thenApply(Function function): 任務A執行完執行B，B需要A的結果，同時B '有返回值'
 *
 */
public class CompletableFutureAPI3Demo {

    public static void main(String[] args) {
        /*
        CompletableFuture.supplyAsync(() -> {
          return 1;
        }).thenApply(f -> {
            return f + 2;
        }).thenApply(f -> {
            return f + 3;
        }).thenAccept(System.out::println);
         */
        // thenRun(Runnable runnable): 任務A執行完執行B，並且B不需要A的結果，A沒有返回值
        System.out.println(CompletableFuture.supplyAsync(() -> "resultA").thenRun(() -> {}).join());
        // thenAccept(Consumer action): 任務A執行完執行B，B需要A的結果，但B '沒有返回值' r為A的返回值
        System.out.println(CompletableFuture.supplyAsync(() -> "resultA").thenAccept(r -> {}).join());
        // thenApply(Function function): 任務A執行完執行B，B需要A的結果，同時B '有返回值' r為A的返回值
        System.out.println(CompletableFuture.supplyAsync(() -> "resultA").thenApply(r -> r + " + resultB").join());
    }

}
