package juc;

import java.util.concurrent.TimeUnit;

/**
 * volatile
 * 1. 保證JMM規範中的2大特性:
 *      * 可見性: 寫入一個volatile變量時，JMM會把該線程對應的本地內存中的共享變量值，立即更新回主內存。
 *               讀取一個volatile變量時，JMM會把該線程對應的本地內存設置為無效，重新回到主內存中讀取最新共享變量。
 *               所以volatile的寫是直接刷新到主內存中，讀是直接從主內存中讀取
 *      * 有序性: 不允許指令重排。編譯器、處理器、JVM為了執行效能，會重排指令執行順序，有時候會改變程序語句的先後順序
 *               不存在數據依賴 => 可以重新排序
 *               "存在數據依賴關係" => 禁止重新排序!
 *               重排後的指令絕不能改變原有的串行語意，這點在併發設計中必須要重點考慮
 *               使用volatile告知以下對象不許重排指令
 *               1. JVM
 *               2. 編譯器: 對於編譯器重排序，JMM會根據重排序的規則，禁止特定類型的編譯器重排序
 *               3. 處理器: 對於處理器重排序，Java編譯器在生成指令排序的適當位置，" 插入內存屏障指令，來禁止特定類型的處理器排序 "
 *      * 不保證原子性: 未加鎖時，線程一對主內存發起read操作到write操作(第一套操作)的間隙時間內，隨時可能有其他線程也對主內存對象發起第二套操作
 *                      ，數據在工作內存中操作的3個步驟中(加載、計算、賦值)是非原子操作的，所以對於修改主內存共享便亮的場景必須使用加鎖同步
 *          * 面試: JVM的自節碼，i++分乘三步，間隙期不同步非原子操作(i++)
 *      * volatile如何保證可見、有序性； 內存屏障Memory Barrier
 * 2. 內存屏障(面試重點):
 *      * 內存屏障是什麼? : 一種屏障指令，它使CPU或編譯器對屏障指令的前和後所發出的內存操作(寫或讀)進行約束，也稱內存柵欄或柵欄指令
 *      * 作用: 阻止屏障兩邊的指令被重排
 *      * 粗分2種:
 *          * 讀屏障: 在讀指令之前插入讀屏障，讓工作內存或CPU高速緩存當中的緩存數據失效，重新回到主內存中獲取最新數據
 *          * 寫屏障: 在寫指令之後插入寫屏障，強制把寫緩衝區的數據刷回到主內存中
 *      * 細分4種:
 *          * 底層C++實作 => Unsafe.class => Unsafe.java => Unsafe.cpp => OrderAccess.cpp => orderAccess_linux_x86.inline.hpp
 *          * orderAccess_linux_x86.inline.hpp
 *          * 4大屏障分別什麼意思:
 *              * OrderAccess::loadload() { acquire(); }
 *              * OrderAccess::storestore() { release(); }
 *              * OrderAccess::loadstore() { acquire(); }
 *              * OrderAccess::storeload() { fence(); }
 *      * 重排序時，不允序把內存屏障之後的指令重排序到內存屏障之前，volatile變量的"寫"一定先發生於任意後續對此volatile變量的"讀"，一定是寫後讀
 *      *
 * 3. 使用時機:
 *  1. 單一賦值可以使用: volatile int a = 10; volatile boolean flag = false; [V]
 *     但含有複合運算賦值不可以: i++ [X]
 *  2. 狀態標示: 判斷業務是否結束
 *  3. 開銷較低的讀、寫鎖策略
 *  4. DCL雙端鎖(雙重檢查)的發佈
 * 4. 總結:
 *  volatile寫之前的操作，都禁止重排序到volatile之後
 *  volatile讀之後的操作，都禁止重排序到volatile之前
 *  volatile寫之後volatile讀，禁止重排序
 */
class MyNumber {
    // volatile 不保證原子性 i++舉例說明
    volatile int number;
    public void addPlusPlus() {
        number++;
    }
}
public class VolatileDemo {
    public static void main(String[] args) {
        MyNumber myNumber = new MyNumber();
        for (int i = 1 ; i <= 10; i++) {
            new Thread(() -> {
                for (int j = 1 ; j <= 1000; j++) {
                    myNumber.addPlusPlus();
                }
            }, String.valueOf(i)).start();
        }
        try { TimeUnit.SECONDS.sleep(2); } catch (InterruptedException e) { e.printStackTrace(); }
        System.out.println(myNumber.number);
    }
}
