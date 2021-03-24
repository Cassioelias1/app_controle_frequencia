package com.example.beacon.api.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.math.BigDecimal;

public class Turma implements Serializable {
    @SerializedName("id")
    private Integer id;//Implementar herança

    @SerializedName("descricao")
    private String descricao;

    @SerializedName("periodo")
    private String periodo;

    @SerializedName("id_beacon_1")
    private String idBeacon1;

    @SerializedName("id_beacon_2")
    private String idBeacon2;

    @SerializedName("id_beacon_3")
    private String idBeacon3;

    @SerializedName("id_beacon_4")
    private String idBeacon4;

    @SerializedName("medida_lado_x")
    private BigDecimal medidaLadoX;

    @SerializedName("medida_lado_y")
    private BigDecimal medidaLadoY;

    @SerializedName("medida_lado_z")
    private BigDecimal medidaLadoZ;

    public Turma() {
    }

    public Turma(String descricao) {
        this.descricao = descricao;
    }

    public Turma(Integer id, String descricao, String periodo) {
        this.id = id;
        this.descricao = descricao;
        this.periodo = periodo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
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

    public String getIdBeacon4() {
        return idBeacon4;
    }

    public void setIdBeacon4(String idBeacon4) {
        this.idBeacon4 = idBeacon4;
    }

    public BigDecimal getMedidaLadoX() {
        return medidaLadoX;
    }

    public void setMedidaLadoX(BigDecimal medidaLadoX) {
        this.medidaLadoX = medidaLadoX;
    }

    public BigDecimal getMedidaLadoY() {
        return medidaLadoY;
    }

    public void setMedidaLadoY(BigDecimal medidaLadoY) {
        this.medidaLadoY = medidaLadoY;
    }

    public BigDecimal getMedidaLadoZ() {
        return medidaLadoZ;
    }

    public void setMedidaLadoZ(BigDecimal medidaLadoZ) {
        this.medidaLadoZ = medidaLadoZ;
    }
}
