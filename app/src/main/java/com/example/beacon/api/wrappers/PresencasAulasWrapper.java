package com.example.beacon.api.wrappers;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PresencasAulasWrapper implements Serializable {
    @SerializedName("data_validacao_localdate")
    private String dataValidacao;

    @SerializedName("descricao")
    private String nomeTurma;

    @SerializedName("academico_id")
    private String academicoId;

    @SerializedName("turma_id")
    private String turma_id;

    @SerializedName("status")
    private String status;

    private boolean withError;

    public PresencasAulasWrapper() {
    }

    public PresencasAulasWrapper(String dataValidacao, String nomeTurma) {
        this.dataValidacao = dataValidacao;
        this.nomeTurma = nomeTurma;
    }

    public String getDataValidacao() {
        return dataValidacao;
    }

    public void setDataValidacao(String dataValidacao) {
        this.dataValidacao = dataValidacao;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isWithError() {
        return withError;
    }

    public void setWithError(boolean withError) {
        this.withError = withError;
    }
}
