package jvm;

/*
1. 虛擬機棧(棧楨中的局部變量區，也稱局部變量表)中引用的對象
2. 方法區中的靜態屬性引用的對象
3. 方法區中常量引用的對象
4. 本地方法棧中JNI(Native方法)引用的對象
 */
public class GCRootDemo {

    private byte[] byteArray = new byte[100 * 1024 * 1024];

    //2.
//    private static GCRootDemo2 t2;
    //3.
//    private static final GCRootDemo3 = new GCRootDemo3(8);

    public static void m1() {
        //1.
        GCRootDemo t1 = new GCRootDemo();
        System.gc();
        System.out.println("第一次GC完成");
    }

    public static void main(String[] args) {
        m1();
    }

}
