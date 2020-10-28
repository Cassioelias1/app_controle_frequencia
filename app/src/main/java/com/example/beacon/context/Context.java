package com.example.beacon.context;

public class Context {
    private static Integer ACADEMICO_ID = null;

    public static Integer getAcademicoId() {
        return ACADEMICO_ID;
    }

    public static void setAcademicoId(Integer academicoId) {
        ACADEMICO_ID = academicoId;
    }
}
