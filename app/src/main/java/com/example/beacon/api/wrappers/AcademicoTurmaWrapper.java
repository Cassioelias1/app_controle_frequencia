package com.example.beacon.api.wrappers;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AcademicoTurmaWrapper implements Serializable {
    @SerializedName("academico_id")
    private String academicoId;

    @SerializedName("turma_id")
    private String turmaId;

    @SerializedName("nome_turma")
    private String nomeTurma;

    public AcademicoTurmaWrapper() {
    }

    public AcademicoTurmaWrapper(String academicoId, String turmaId, String nomeTurma) {
        this.academicoId = academicoId;
        this.turmaId = turmaId;
        this.nomeTurma = nomeTurma;
    }

    public String getAcademicoId() {
        return academicoId;
    }

    public void setAcademicoId(String academicoId) {
        this.academicoId = academicoId;
    }

    public String getTurmaId() {
        return turmaId;
    }

    public void setTurmaId(String turmaId) {
        this.turmaId = turmaId;
    }

    public String getNomeTurma() {
        return nomeTurma;
    }

    public void setNomeTurma(String nomeTurma) {
        this.nomeTurma = nomeTurma;
    }
}
