package com.example.beacon.context;

public class AppContext {
    private static Integer ACADEMICO_ID = null;
    private static Integer AULA_ID_SELECT = null;

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
}
