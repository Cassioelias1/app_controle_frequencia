package com.example.beacon.models;

import java.math.BigDecimal;

public class ContantesDistancia {
    private int rssi;
    private Double constanteA;
    private Double constanteB;
    private Double constanteC;

    public ContantesDistancia() {
    }

    public ContantesDistancia(int rssi, Double constanteA, Double constanteB, Double constanteC) {
        this.rssi = rssi;
        this.constanteA = constanteA;
        this.constanteB = constanteB;
        this.constanteC = constanteC;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
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
}
