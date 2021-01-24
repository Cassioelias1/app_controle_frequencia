package com.example.beacon.api.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Presenca implements Serializable {
    @SerializedName("id_academico")
    private String idAcademico;

    @SerializedName("id_beacon_1")
    private String idBeacon1;

    @SerializedName("id_beacon_2")
    private String idBeacon2;

    @SerializedName("id_beacon_3")
    private String idBeacon3;

    @SerializedName("id_turma")
    private String idTurma;

    @SerializedName("data")
    private String data;

    @SerializedName("mensagemRetorno")
    private String mensagemRetorno;

    @SerializedName("idsBeacons")
    private String idsBeacons;

    @SerializedName("status")
    private String status;//transformar em enum

    @SerializedName("posicao_x")
    private String posicaoX;

    @SerializedName("posicao_y")
    private String posicaoY;

    public Presenca() {}

    public Presenca(String idAcademico, String idBeacon1, String idBeacon2, String idBeacon3, String data) {
        this.idAcademico = idAcademico;
        this.idBeacon1 = idBeacon1;
        this.idBeacon2 = idBeacon2;
        this.idBeacon3 = idBeacon3;
        this.idTurma = idTurma;
        this.data = data;
    }

    public String getIdAcademico() {
        return idAcademico;
    }

    public void setIdAcademico(String idAcademico) {
        this.idAcademico = idAcademico;
    }

    public String getIdBeacon1() {
        return idBeacon1;
    }

    public void setIdBeacon1(String idBeacon1) {
        this.idBeacon1 = idBeacon1;
    }

    public String getIdBeacon2() {
        return idBeacon2;
    }

    public void setIdBeacon2(String idBeacon2) {
        this.idBeacon2 = idBeacon2;
    }

    public String getIdBeacon3() {
        return idBeacon3;
    }

    public void setIdBeacon3(String idBeacon3) {
        this.idBeacon3 = idBeacon3;
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

    public String getIdsBeacons() {
        return idsBeacons;
    }

    public void setIdsBeacons(String idsBeacons) {
        this.idsBeacons = idsBeacons;
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
}
