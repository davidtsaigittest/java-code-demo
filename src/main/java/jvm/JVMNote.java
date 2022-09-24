package jvm;

import java.util.Random;

/**
1 JVM系統架構

 2 類加載器
    2.1 有幾種類加載器 BootstrapClassLoader
                     extensionClassLoader
                     appClassLoader
    2.2 雙親委派
    2.3 沙箱安全機制
 3 Native
    3.1 native關鍵字
    3.2 聲明有, 實現無 why?
 4 PC寄存器
    4.1 記錄了方法之間的調用和執行情況，類似排班值日表
        用來儲存指向下一條指令的地址，即將要執行的指令代碼
        他是當前線程所執行的字節碼的行號指示器

 5 方法區
   由Classloader將.class檔案內容載入JVM方法區中生成Class
   5.1存儲:
    1.每個類的結構訊息，作為生成實例對象的模板，即Class
    2.運行時常量池(Runtime Constant Pool)
    3.字段和方法數據
    4.構造函數、static修飾的(方法、塊、變量):靜態先行加載一次
    注意: 實例(非static)變量、實例方法存在於堆內存中，不在方法區
   5.2 方法區是規範，在不同虛擬機內的時現不同
    典型實現: java 8前 永生帶PermGen space，java 8後 元空間Metaspace

 6 stack: java方法存在於虛擬機的棧空間時->稱為棧幀
    6.1 棧管運行，堆管存儲
    6.2 棧保存哪寫東西 ? A:8種基本類型的變量+對象的引用變量+實例方法都是在函數的棧內存中分配
        本地變量(Local Variables): 輸入參數+輸出參數+方法內的變量
        棧操作(Operand Stack): 紀錄出棧、入棧的操作
        棧幀數據(Frame Data): 包括類文件、方法等等
    6.3 Exception in thread "main" java.lang.StackOverflowError

 7 heap --> 對象生命週期 --> OOM
    7.1 使用JVM指令驗證理論上的heap架構是否正確
    新生代Young(eden + survivor0 + survivor1): 占 Heap空間 1/3
    老生代Old: 占Heap空間 2/3
 8 GC
    GC是什麼(分代收集算法:沒有最好的算法，要根據不同代特性使用最適合的算法)
        次數上頻繁收集Young區
        次數上較少收集Old區
        基本不動元空間
    GC算法總體概述:
        Major GC耗時比Minor GC長10倍
    四大算法
        1.引用計數法: 沒用
            缺點:
                1.每次對象賦值要維護引用計數器，且計數器本身也有一定消耗
                2.較難處理循環引用(ex: A對象引用B對象且B對象同時也引用A)
        2.複製算法(Copying): 最常用
            1. YoungGC使用
                浪費內存空間
                不產生內存碎片(不連續內存)
        3.標記清除(Mark-Sweep)
            1. 老年代使用
            2. 節省內存空間
            3. 缺點:
                耗時
                產生內存碎片
        4.標記壓縮(Mark-Compact): 標記清除+整理算法
            1.老年代使用
            2.缺點:
                耗時嚴重
        實際工作上:
            使用標記-清除-壓縮(Mark-Sweep-Compact)
            原理:
                1.Mark-Sweep和Mark-Compact結合
                2.和Mark-Sweep一致，當進行多次GC後才會Compact，減少移動對象的成本
        * Java 9 G1算法 有改善號時長的問題 *
    GCRoot: 改善引用計數法循環依賴問題
        1. 解決方式: 枚舉跟節點做可達性分析(根搜索路徑)
        2. 可作為GC Root的對象有:
            1. 虛擬機棧(棧楨中的局部變量區，也稱局部變量表)中引用的對象
            2. 方法區中的靜態屬性引用的對象
            3. 方法區中常量引用的對象
            4. 本地方法棧中JNI(Native方法)引用的對象
    JVM調參:
        參數類型:
            1. 標配參數: -help, -version, -showperson
            2. X參數(了解就行): -Xint解釋執行, -Xcomp第一次使用就編譯成本地代碼, -Xmixed混和模式
            3. XX參數(重要!!工作常用):
                * Boolean類型:
                    * 公式: -XX:+(表示開啟)或-XX:-(表示關閉)
                    * Case舉例:
                        * 是否打印GC收集細節: -XX:-PrintGCDetails(停用), -XX:+PrintGCDetails(啟用)
                        * 是否使用串行垃圾回收器: -XX:-UseSerialGC(停用), -XX:+UseSerialGC(啟用)
                * KV設值類型
                    * 公式: -XX:屬性key=屬性值value
                    * Case舉例:
                        * -XX:MetaspaceSize=128m
                        * -XX:MaxTenuringThreshold=15
        jinfo舉例查看當前運行程序的配置:


 */
public class JVMNote {

    public static void main(String[] args) {
        // 重要考點: 用遞迴方法調用製造StackOverFlowError(SOF)
        // stackOverflow();
//        getJVMMemorySize();
        outOfMemory();
    }


    /**
     * 實際生產:
     * 避免GC和應用爭搶內存時，造成內存在設定區間內忽高忽低
     * -Xms, -Xmx要配置成一樣大
     */
    public static void getJVMMemorySize() {
        // 初始分配內存，默認為物理內存的 1/64
        long totalMemory = Runtime.getRuntime().totalMemory();
        // 最大分配內存，默認為物理內存的 1/4
        long maxMemory = Runtime.getRuntime().maxMemory();
        System.out.println("-Xms:TOTAL_MEMORY = " + totalMemory + " (byte), " + (totalMemory / (double)1024 / 1024) + "MB");
        System.out.println("-Xmx:MAX_MEMORY = " + maxMemory + " (byte), " + (maxMemory / (double)1024 / 1024) + "MB");
    }

    public static void outOfMemory() {
//        byte[] bytes = new byte[900 * 1024 * 1024];
        String str = "This is a random str";
        while (true) {
            str += str + new Random().nextInt(88888888) + new Random().nextInt(99999999);
        }
    }

    public static void stackOverflow() {
        m2();
    }

    public static void m2() {
        stackOverflow();
    }
}
