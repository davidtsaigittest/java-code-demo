package juc;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * applyToEither
 * 對計算速度進行選用(工作中常用): 誰快用誰
 */
public class CompletableFutureFastDemo {

    public static void main(String[] args) {
        CompletableFuture<String> playerA = CompletableFuture.supplyAsync(() -> {
            System.out.println("A come in");
            try { TimeUnit.SECONDS.sleep(2); } catch (InterruptedException e) { e.printStackTrace(); }
            return "playerA";
        });
        CompletableFuture<String> playerB = CompletableFuture.supplyAsync(() -> {
            System.out.println("B come in");
            try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
            return "playerB";
        });
        CompletableFuture<String> result = playerA.applyToEither(playerB, f -> {
            return f + " is winner";
        });
        System.out.println(Thread.currentThread().getName() + "\t " + "--------: " + result.join());
    }
}
