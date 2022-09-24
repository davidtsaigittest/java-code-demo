package jvm;

/**
 * 類加載順序 1. static塊 > 2.構造塊 > 3.構造方法
 * static塊只加載一次
 * 有new會執行構造塊和構造方法
 */
class CodeZY {
    public CodeZY() {
        System.out.println("Code的構造方法1111");
    }
    {
        System.out.println("Code的構造塊2222");
    }
    static {
        System.out.println("Code的靜態代碼塊3333");
    }
}
public class CodeBlock03 {
    String str;
    {
        System.out.println("CodeBlock03的構造塊444");
    }
    //1
    static {
        System.out.println("CodeBlock03的靜態代碼塊555");
    }
    public CodeBlock03() {
        System.out.println("CodeBlock03的構造方法666");
    }

    public CodeBlock03(String str) {
        System.out.println("CodeBlock03的參數構造方法888");
        this.str = str;
    }

    public static void main(String[] args) {
        //2
        System.out.println("我是分割線=======CodeBlock03的main方法777");

        new CodeZY();
        System.out.println("------------------");
        new CodeZY();
        System.out.println("------------------");
        new CodeBlock03();
        new CodeBlock03("");
    }
}
