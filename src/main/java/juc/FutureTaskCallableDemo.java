package juc;

import java.util.concurrent.*;

class MyThread implements Callable<Integer> {
    @Override
    public Integer call() throws Exception {
        System.out.println("**********come in Callable");
        return 1024;
    }

}

/**
 * 為何需要Callable相較Runnable
 * 1. Callable有返回值可以取得該線程運算結果
 * 2. 可以拋出異常，了解錯誤發生原因
 *
 * 多線程情況如何使用Callable與Thread類
 * Thread(Runnable target, String name) => Thread 構造器只接受Runnable
 * => 需要一實作Runnable的類，該類又支持Callable => 使用FutureTask
 *
 * FutureTask類似一Adapter將Thread與Callable連結起來
 *
 * 使用FutureTask可滿足併發情況下的三個特點:
 * 1. 多線程: 實作Runnable接口->RunnableFuture繼承Runnable接口
 * 2. 有返回值: Callable->FutureTask構造器注入
 * 3. 異步任務: 可以被叫停取消，可以查看運行是否完成->RunnableFuture繼承Future接口
 * 利用以上異步&返回值特性應用Fork/Join分支合併思想: 可降低耗時線程的阻塞問題，提升效能
 *
 *
 * FutureTask缺點:
 * 1.調用FutureTask.get()會有堵塞問題 -> 改用CompletableFutureTask
 * 2.isDone()輪洵
 * 3.較難處理複雜任務
 *
 * FutureTask異步任務需要優化:
 * 1.可以主動通知任務已經完成(回調通知)
 * 2.改善輪詢，搶占CPU資源，代碼優化
 * 3.異步任務計算互相依賴(後一異步計算，需依賴前一個異步計算結果)
 *   將多個異步計算合成一個異步計算，這幾哦異步計算互相獨立
 * 4.返回Future集合內最快計算完畢的結果
 *
 * -> 引入CompletableFuture since jdk 1.8
 * 1. 盡量使用靜態方法runAsync(), supplyAsync() 建立異步任務，不要使用CompletableFuture(){ }構造器
 */
public class FutureTaskCallableDemo {



    public static void main(String[] args) throws ExecutionException, InterruptedException, TimeoutException {
//        futureTask();
    }

    private static void futureTask() throws InterruptedException, ExecutionException {
        //FutureTask(Callable<V> callable)
        FutureTask<Integer> futureTask = new FutureTask<>(new MyThread());
        new Thread(futureTask, "AA").start();
        // 多個線程執行同一個futureTask會複用 => thread BB不會執行
        new Thread(futureTask, "BB").start();

        // 與線程池搭配使用，避免不斷new造成GC，複用以前的線程可提升效能->改用線程池
        ExecutorService threadPool = Executors.newFixedThreadPool(3);
        threadPool.submit(futureTask);

        // 執行完後要釋放資源
        threadPool.shutdown();

        System.out.println(Thread.currentThread().getName() + "************");
        int result01 = 100;

        // 遇複雜情況檢查futureTask是否算完，直到算完才跳出循環
        // !取反，類似自旋鎖，不停輪詢會消耗CPU，也不一定可取得及時計算結果
//        while (!futureTask.isDone()) {
//        }

        while(true) {
            if (futureTask.isDone()) {
                System.out.println(futureTask.get());
            } else {
                try { TimeUnit.MILLISECONDS.sleep(500); } catch (InterruptedException e) { e.printStackTrace(); }
                System.out.println("處理中......");
            }
        }

        //要求獲取Callable線程的計算結果，若沒有計算完成就要去強求get()，會導致堵塞，建議放在最後
        //System.out.println("*******result: " + futureTask.get());
        /*
        設定3秒後拿不到結果就拋出異常java.util.concurrent.TimeoutException，屬於強行打斷，可用但不優雅
        futureTask.get(3, TimeUnit.SECONDS);
         */
    }


}
