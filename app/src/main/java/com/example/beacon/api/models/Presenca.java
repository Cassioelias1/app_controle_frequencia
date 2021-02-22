package com.example.beacon.api.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.math.BigDecimal;

public class Presenca implements Serializable {
    @SerializedName("academico_id")
    private String idAcademico;

    @SerializedName("turma_id")
    private String idTurma;

    @SerializedName("descricao")
    private String descricao;//descricao da turma

    @SerializedName("data")
    private String data;

    @SerializedName("mensagemRetorno")
    private String mensagemRetorno;

    @SerializedName("status")
    private String status;//transformar em enum

    @SerializedName("material_card_id")
    private String materialCardId;

    @SerializedName("text_view_id")
    private String textViewId;

    @SerializedName("posicao_x")
    private BigDecimal posicaoX;//Referente ao plano zy / Beacon2

    @SerializedName("posicao_y")
    private BigDecimal posicaoY;//Referente ao plano xz / Beacon1

    @SerializedName("posicao_z")
    private BigDecimal posicaoZ;//Referente ao plano xy / Beacon3

    //adicionar os fields de PosicaoAcademico ou fazer um include

    private Integer idSqlite;

    public Presenca() {}

    public Presenca(String idAcademico, String turmaId, String data, String materialCardName, String textViewId) {
        this.idAcademico = idAcademico;
        this.idTurma = turmaId;
        this.data = data;
        this.materialCardId = materialCardName;
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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getPosicaoX() {
        return posicaoX;
    }

    public void setPosicaoX(BigDecimal posicaoX) {
        this.posicaoX = posicaoX;
    }

    public BigDecimal getPosicaoY() {
        return posicaoY;
    }

    public void setPosicaoY(BigDecimal posicaoY) {
        this.posicaoY = posicaoY;
    }

    public BigDecimal getPosicaoZ() {
        return posicaoZ;
    }

    public void setPosicaoZ(BigDecimal posicaoZ) {
        this.posicaoZ = posicaoZ;
    }

    public void setPosicaoAcademicoHorarioAulaAndSetStatus(PosicaoAcademico posicaoAcademico){
        if (posicaoAcademico == null){
            this.status = "AUSENTE";
        } else {
            this.status = "PRESENTE";
            this.posicaoX = posicaoAcademico.getPosicaoX();
            this.posicaoY = posicaoAcademico.getPosicaoY();
            this.posicaoZ = posicaoAcademico.getPosicaoZ();
        }
    }
}
