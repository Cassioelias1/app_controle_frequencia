package com.example.beacon.models;

import java.math.BigDecimal;

public class Beacon {

    //Combinação dos ids(1,2,3) do Beacon.
    private String identificador;
    private BigDecimal posicaoX;
    private BigDecimal posicaoY;
    private BigDecimal posicaoZ;

    public Beacon() {
    }

    public Beacon(BigDecimal posicaoX, BigDecimal posicaoY, BigDecimal posicaoZ) {
        this.posicaoX = posicaoX;
        this.posicaoY = posicaoY;
        this.posicaoZ = posicaoZ;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
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
}
