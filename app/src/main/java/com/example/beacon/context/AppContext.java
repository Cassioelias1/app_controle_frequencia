package com.example.beacon.context;

import android.app.NotificationManager;

public class AppContext {
    private static Integer ACADEMICO_ID = null;
    private static Integer AULA_ID_SELECT = null;
    private static NotificationManager notificationManager = null;

    //thread
    private static Thread thread1915 = null;
    private static Thread thread2015 = null;
    private static Thread thread2100 = null;
    private static Thread thread2140 = null;

    public static Integer getAcademicoId() {
        return ACADEMICO_ID;
    }

    public static void setAcademicoId(Integer academicoId) {
        ACADEMICO_ID = academicoId;
    }

    public static Integer getAulaIdSelect() {
        return AULA_ID_SELECT;
    }

    public static void setAulaIdSelect(Integer aulaIdSelect) {
        AULA_ID_SELECT = aulaIdSelect;
    }

    public static NotificationManager getNotificationManager() {
        return notificationManager;
    }

    public static void setNotificationManager(NotificationManager notificationManager) {
        AppContext.notificationManager = notificationManager;
    }

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
}
