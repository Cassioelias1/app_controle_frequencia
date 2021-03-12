package com.example.beacon.context;

import android.app.NotificationManager;

public class AppContext {
    private static String ACADEMICO_ID = null;
    private static String TURMA_ID = null;
    private static String NOME_TURMA = null;
    public static String DATA_VALIDACAO_SELECTED = null;
    private static NotificationManager notificationManager = null;

    //thread
    private static Thread thread1915 = null;
    private static Thread thread2015 = null;
    private static Thread thread2100 = null;
    private static Thread thread2140 = null;

    //representação da turma do dia
    private static String ID_BEACON_1 = null;
    private static String ID_BEACON_2 = null;
    private static String ID_BEACON_3 = null;
    private static String ID_BEACON_4 = null;

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

    public static String getIdBeacon1() {
        return ID_BEACON_1;
    }

    public static void setIdBeacon1(String idBeacon1) {
        ID_BEACON_1 = idBeacon1;
    }

    public static String getIdBeacon2() {
        return ID_BEACON_2;
    }

    public static void setIdBeacon2(String idBeacon2) {
        ID_BEACON_2 = idBeacon2;
    }

    public static String getIdBeacon3() {
        return ID_BEACON_3;
    }

    public static void setIdBeacon3(String idBeacon3) {
        ID_BEACON_3 = idBeacon3;
    }

    public static String getIdBeacon4() {
        return ID_BEACON_4;
    }

    public static void setIdBeacon4(String idBeacon4) {
        ID_BEACON_4 = idBeacon4;
    }
}
