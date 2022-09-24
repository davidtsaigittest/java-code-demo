package jvm;


/**ClassLoader
 * 1. 用途:.class文件
 * 2. 種類
 * 3. 雙親委派(類加載執行順序BootStrapClassLoader->extensionClassLoader->AppClassLoader)
 * 4. 雙親委派機制->保證沙箱安全(保證自己寫的代碼不會汙染jdk內原生代碼)
 */
public class MyObject {

    static {
        System.out.println("static block");
    }

    public static void main(String[] args) {
        MyObject myObject = new MyObject();
        System.out.println(myObject.getClass().getClassLoader());
    }
    
    
}
