package juc;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * 對列阻塞有沒有好的一面?
 *
 * 當不得不阻塞時，你如何管理
 */
public class BlockingQueueDemo {

    public static void main(String[] args) {
        BlockingQueue<String> blockingQueue  = new ArrayBlockingQueue<>(3);
    }

}
