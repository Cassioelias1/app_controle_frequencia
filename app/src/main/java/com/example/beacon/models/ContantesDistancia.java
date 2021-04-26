package com.example.beacon.models;

import java.math.BigDecimal;

public class ContantesDistancia {
    private Double constanteA;
    private Double constanteB;
    private Double constanteC;
    private int rssiUmMetro;

    public ContantesDistancia() {
    }

    public ContantesDistancia(int rssiUmMetro, Double constanteA, Double constanteB, Double constanteC) {
        this.rssiUmMetro = rssiUmMetro;
        this.constanteA = constanteA;
        this.constanteB = constanteB;
        this.constanteC = constanteC;
    }

    public Double getConstanteA() {
        return constanteA;
    }

    public void setConstanteA(Double constanteA) {
        this.constanteA = constanteA;
    }

    public Double getConstanteB() {
        return constanteB;
    }

    public void setConstanteB(Double constanteB) {
        this.constanteB = constanteB;
    }

    public Double getConstanteC() {
        return constanteC;
    }

    public void setConstanteC(Double constanteC) {
        this.constanteC = constanteC;
    }

    public int getRssiUmMetro() {
        return rssiUmMetro;
    }

    public void setRssiUmMetro(int rssiUmMetro) {
        this.rssiUmMetro = rssiUmMetro;
    }
}
