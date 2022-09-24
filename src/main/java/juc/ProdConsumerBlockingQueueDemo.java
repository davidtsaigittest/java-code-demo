package juc;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

class MyResource {
    private volatile boolean flag = true; // 默認開啟, 進行生產+消費
    private AtomicInteger atomicInteger = new AtomicInteger();

    BlockingQueue<String> blockingQueue = null;

    public MyResource(BlockingQueue<String> blockingQueue) {
        this.blockingQueue = blockingQueue;
        System.out.println(blockingQueue.getClass().getName());
    }

    public void myProducer() throws Exception {
        String data = null;
        boolean rtnValue;
        while (flag) {
            data = atomicInteger.incrementAndGet() + "";
            rtnValue = blockingQueue.offer(data, 2L, TimeUnit.SECONDS);
            if (rtnValue) {
                System.out.println(Thread.currentThread().getName() + "\t 插入對列" + data + "成功");
            } else {
                System.out.println(Thread.currentThread().getName() + "\t 插入對列" + data + "失敗");
            }
            TimeUnit.SECONDS.sleep(1);
        }
        System.out.println(Thread.currentThread().getName() + "\t大老闆叫停了，表示flag=false，生產結束了");
    }

    public void myConsumer() throws Exception {
        String result = null;
        while (flag) {
            result = blockingQueue.poll(2L, TimeUnit.SECONDS);
            if (null == result || result.equalsIgnoreCase("")) {
                flag = false;
                System.out.println(Thread.currentThread().getName() + "\t 超過2秒沒有取到蛋糕, 消費退出");
                System.out.println();
                System.out.println();
                return;
            }
            System.out.println(Thread.currentThread().getName() + "\t 消費隊列蛋糕" + result + "成功");
        }
    }

    public void stop() throws Exception {
        this.flag = false;
    }

}

/**
 * volatile/CAS/atomicInteger/BlockQueue/線程交互/原子引用
 */
public class ProdConsumerBlockingQueueDemo {

    public static void main(String[] args) throws Exception {
        MyResource myResource = new MyResource(new ArrayBlockingQueue<>(10));

        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "\t 生產線程啟動");
            try {
                myResource.myProducer();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "Producer").start();

        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "\t 消費線程啟動");
            System.out.println();
            System.out.println();
            try {
                myResource.myConsumer();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "Consumer").start();

        try { TimeUnit.SECONDS.sleep(5); } catch (InterruptedException e) { e.printStackTrace(); }

        System.out.println();
        System.out.println();
        System.out.println();

        System.out.println("5秒鐘到了，大老闆main線程叫停，活動結束");

        myResource.stop();
    }

}
