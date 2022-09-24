package juc;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * 功能:
 * 獲取計算結果
 *  get()
 *  get(long timeout, TimeUnit unit)
 *  join()
 *  getNow(T valueIfAbsent) 返回一實際值
 * 觸發計算
 *  complete(T value) 返回一boolean
 */
public class CompletableFutureAPIDemo {

    public static void main(String[] args) //throws ExecutionException, InterruptedException, TimeoutException
    {

        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "abc";
        });
        try { TimeUnit.SECONDS.sleep(2); } catch (InterruptedException e) { e.printStackTrace(); }
//        completableFuture.get(); // 無返回拋錯
//        completableFuture.get(2L, TimeUnit.SECONDS); // 指定時間內無返回拋錯
//        completableFuture.join(); // 未計算完不拋錯
//        completableFuture.getNow("xxx"); //沒有計算完成的情況下，返回一替代值xxx，獲取結果不堵塞
//        completableFuture.complete("abc"); // 是否打斷get方法立即返回""內值
        // complete("xxx")可搭配join()做判斷使用 未計算完也不會拋出異常
        System.out.println(completableFuture.complete("completeValue") + "\t" + completableFuture.join());
//        try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
    }


}
