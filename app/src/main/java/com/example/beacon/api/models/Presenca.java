package com.example.beacon.api.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Presenca implements Serializable {
    @SerializedName("academico_id")
    private String idAcademico;

    @SerializedName("turma_id")
    private String idTurma;

    @SerializedName("data")
    private String data;

    @SerializedName("mensagemRetorno")
    private String mensagemRetorno;

    @SerializedName("status")
    private String status;//transformar em enum

    @SerializedName("posicao_x")
    private String posicaoX;

    @SerializedName("posicao_y")
    private String posicaoY;

    @SerializedName("material_card_id")
    private String materialCardId;

    @SerializedName("text_view_id")
    private String textViewId;

    private Integer idSqlite;

    public Presenca() {}

    public Presenca(String idAcademico, String turmaId, String data, String materialCardId, String textViewId) {
        this.idAcademico = idAcademico;
        this.idTurma = turmaId;
        this.data = data;
        this.materialCardId = materialCardId;
        this.textViewId = textViewId;
    }

    public String getIdAcademico() {
        return idAcademico;
    }

    public void setIdAcademico(String idAcademico) {
        this.idAcademico = idAcademico;
    }

    public String getIdTurma() {
        return idTurma;
    }

    public void setIdTurma(String idTurma) {
        this.idTurma = idTurma;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getMensagemRetorno() {
        return mensagemRetorno;
    }

    public void setMensagemRetorno(String mensagemRetorno) {
        this.mensagemRetorno = mensagemRetorno;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPosicaoX() {
        return posicaoX;
    }

    public void setPosicaoX(String posicaoX) {
        this.posicaoX = posicaoX;
    }

    public String getPosicaoY() {
        return posicaoY;
    }

    public void setPosicaoY(String posicaoY) {
        this.posicaoY = posicaoY;
    }

    public void setStatusTrilateracao(boolean estaDentroSalaAula){
        this.status = estaDentroSalaAula ? "PRESENTE" : "AUSENTE";
    }

    public String getMaterialCardId() {
        return materialCardId;
    }

    public void setMaterialCardId(String materialCardId) {
        this.materialCardId = materialCardId;
    }

    public String getTextViewId() {
        return textViewId;
    }

    public void setTextViewId(String textViewId) {
        this.textViewId = textViewId;
    }

    public Integer getIdSqlite() {
        return idSqlite;
    }

    public void setIdSqlite(Integer idSqlite) {
        this.idSqlite = idSqlite;
    }
}
