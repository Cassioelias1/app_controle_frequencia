package com.example.beacon.api.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Presenca implements Serializable {
    @SerializedName("id_academico")
    private String idAcademico;

    @SerializedName("id_beacon")
    private String idBeacon;

    @SerializedName("id_turma")
    private String idTurma;

    @SerializedName("data")
    private String data;

    @SerializedName("mensagemRetorno")
    private String mensagemRetorno;

    public Presenca() {
    }

    public Presenca(String idAcademico, String idBeacon, String data) {
        this.idAcademico = idAcademico;
        this.idBeacon = idBeacon;
        this.data = data;
    }

    public String getIdAcademico() {
        return idAcademico;
    }

    public void setIdAcademico(String idAcademico) {
        this.idAcademico = idAcademico;
    }

    public String getIdBeacon() {
        return idBeacon;
    }

    public void setIdBeacon(String idBeacon) {
        this.idBeacon = idBeacon;
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
}
