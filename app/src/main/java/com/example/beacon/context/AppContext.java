package com.example.beacon.context;

public class AppContext {
    public static String DATA_VALIDACAO_SELECTED = null;

    //Thread
    private static Thread thread1915 = null;
    private static Thread thread2015 = null;
    private static Thread thread2100 = null;
    private static Thread thread2140 = null;

    public static Thread getThread1915() {
        return thread1915;
    }

    public static void setThread1915(Thread thread1915) {
        AppContext.thread1915 = thread1915;
    }

    public static Thread getThread2015() {
        return thread2015;
    }

    public static void setThread2015(Thread thread2015) {
        AppContext.thread2015 = thread2015;
    }

    public static Thread getThread2100() {
        return thread2100;
    }

    public static void setThread2100(Thread thread2100) {
        AppContext.thread2100 = thread2100;
    }

    public static Thread getThread2140() {
        return thread2140;
    }

    public static void setThread2140(Thread thread2140) {
        AppContext.thread2140 = thread2140;
    }

    public static String getDataValidacaoSelected() {
        return DATA_VALIDACAO_SELECTED;
    }

    public static void setDataValidacaoSelected(String dataValidacaoSelected) {
        DATA_VALIDACAO_SELECTED = dataValidacaoSelected;
    }
}
