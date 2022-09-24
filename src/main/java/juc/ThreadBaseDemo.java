package juc;

/**
 *  Thread start方法底層調用native start0方法
 *  底層透過JNI調用c,c++控制操作系統
 *  openjdk8\hotspot\src\share\Thread.c
 *  openjdk8\hotspot\src\share\vm\prims\jvm.cpp
 *  openjdk8/hotspot\src]share\vm\runtime\thread.cpp
 *  Thread.java中的native start0方法 對應到 Thread.c中的JVM_StartThread方法
 *  JVM_StartThread => 調用jvm.cpp 2883行執行 Thread::start(native_thread)
 *  => 調用thread.cpp中的Thread::start方法
 *  => 執行os::start_thread(thread)
 *  => 通知操作系統要分配一個線程
 *  Initialize the thread state to RUNNABLE before starting
 *  this thread... (程式碼註解)
 *  =>因此Thread start()方法執行後，底層執行時僅將線程狀態更改狀態為"可執行"並非直接啟動線程
 *  實際線程何時啟動我們無法控制，交由操作系統自行決定
 */
public class ThreadBaseDemo {

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {

        }, "t1");
        t1.start();
    }

}
