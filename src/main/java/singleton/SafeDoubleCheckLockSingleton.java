package singleton;

/**
 * 單例模式寫法:DCL雙重鎖定檢查
 */
public class SafeDoubleCheckLockSingleton {

    // 注意!要加上volatile聲明，實現線程安全的延遲初始化
    private volatile static SafeDoubleCheckLockSingleton singleton;
    // 私有化構造方法
    private SafeDoubleCheckLockSingleton() {}
    // 雙重鎖設計
    public static SafeDoubleCheckLockSingleton getInstance() {
        if (singleton == null) {
            // 1.多線程併發創建對象時，會通過加鎖機制保證只有一個線程能創建對象
            synchronized (SafeDoubleCheckLockSingleton.class) {
                if (singleton == null) {

                    // 隱患: 多線程環境下，由於指令重排序，該對象可能還未完成初始化就被其他線程讀取

                    /*
                        new創建對象包含過程有(step1堆分配內存空間、step2初始化對象、step3將對象指向分配的內存空間)，
                        某些編譯器為了性能問題，會將step2, step3進行重排序，這樣可能造程其他線程，
                        獲得一個未完全初始化的實例
                     */
                    // 解決: 利用volatile，禁止"step2初始化對象"和"step3將對象指向分配的內存空間"的重排序
                    singleton = new SafeDoubleCheckLockSingleton();
                }
            }
            // 2.對象創建完畢，執行getInstance()將不需要獲取鎖，直接返回創建對象
        }
        return singleton;
    }

}
