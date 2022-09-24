package juc.countdownlatch;

import java.util.concurrent.CountDownLatch;

public class CountDownLatchDemo {

    private static final Integer countDown = 4;


    public static void main(String[] args) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(countDown);

        for (int i = 1; i <= 4; i++) {
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + "\t season is passed.");
                countDownLatch.countDown();
            }, SeasonEnum.forEachSeasonEnum(i).getEngName().toUpperCase()).start();
        }
        countDownLatch.await();
        System.out.println(Thread.currentThread().getName() + "\t One year passed !!");
    }
}
