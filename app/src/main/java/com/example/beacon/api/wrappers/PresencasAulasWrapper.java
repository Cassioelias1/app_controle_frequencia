package com.example.beacon.api.wrappers;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PresencasAulasWrapper implements Serializable {
    @SerializedName("data_presenca")
    private String dataPresenca;

    @SerializedName("nome_turma")
    private String nomeTurma;

    @SerializedName("academico_id")
    private String academicoId;

    @SerializedName("turma_id")
    private String turma_id;

    public PresencasAulasWrapper() {
    }

    public PresencasAulasWrapper(String dataPresenca, String nomeTurma) {
        this.dataPresenca = dataPresenca;
        this.nomeTurma = nomeTurma;
    }

    public String getDataPresenca() {
        return dataPresenca;
    }

    public void setDataPresenca(String dataPresenca) {
        this.dataPresenca = dataPresenca;
    }

    public String getNomeTurma() {
        return nomeTurma;
    }

    public void setNomeTurma(String nomeTurma) {
        this.nomeTurma = nomeTurma;
    }

    public String getAcademicoId() {
        return academicoId;
    }

    public void setAcademicoId(String academicoId) {
        this.academicoId = academicoId;
    }

    public String getTurma_id() {
        return turma_id;
    }

    public void setTurma_id(String turma_id) {
        this.turma_id = turma_id;
    }
}
