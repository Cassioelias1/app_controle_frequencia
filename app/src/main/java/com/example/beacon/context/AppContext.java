package com.example.beacon.context;

import android.app.NotificationManager;

public class AppContext {
    private static String ACADEMICO_ID = null;
    private static String TURMA_ID = null;
    private static String NOME_TURMA = null;
    private static NotificationManager notificationManager = null;

    //thread
    private static Thread thread1915 = null;
    private static Thread thread2015 = null;
    private static Thread thread2100 = null;
    private static Thread thread2140 = null;

    public static String getNomeTurma() {
        return NOME_TURMA;
    }

    public static void setNomeTurma(String nomeTurma) {
        NOME_TURMA = nomeTurma;
    }

    public static String getAcademicoId() {
        return ACADEMICO_ID;
    }

    public static void setAcademicoId(String academicoId) {
        ACADEMICO_ID = academicoId;
    }

    public static String getTurmaId() {
        return TURMA_ID;
    }

    public static void setTurmaId(String turmaId) {
        TURMA_ID = turmaId;
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
