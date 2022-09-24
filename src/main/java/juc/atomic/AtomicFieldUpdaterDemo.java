package juc.atomic;

/**
 * 對象的屬性修改原子類
 * AtomicIntegerFieldUpdater
 * AtomicLongFieldUpdater
 * AtomicReferenceFieldUpdater
 * 1. 加鎖非整個對象，只針對特定屬性確保操作原子性
 * 2. 使用要求 :
 *      * 更新的屬性必須使用volatile修飾
 *      * 因屬性修改原子類(AtomicFieldUpdater類)都是抽象類
 *       ，所以每次使用都必須使用靜態方法newUpdater()創建一個更新器
 *       ，並需要設置想要更新的類和屬性
 * 3.面試問題: 你在哪裡用過volatile?使用情境是?
 *  * 雙端檢索單例模式
 *  * AtomicReferenceFieldUpdater
 */
public class AtomicFieldUpdaterDemo {
}
