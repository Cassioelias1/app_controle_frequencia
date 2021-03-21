package com.example.beacon.models;

import java.math.BigDecimal;

public class Beacon {

    //Combinação dos ids(1,2,3) do Beacon.
    private String identificador;
    private double posicaoX;
    private double posicaoY;
    private double posicaoZ;

    public Beacon() {
    }

    public Beacon(double posicaoX, double posicaoY, double posicaoZ) {
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

    public double getPosicaoX() {
        return posicaoX;
    }

    public void setPosicaoX(double posicaoX) {
        this.posicaoX = posicaoX;
    }

    public double getPosicaoY() {
        return posicaoY;
    }

    public void setPosicaoY(double posicaoY) {
        this.posicaoY = posicaoY;
    }

    public double getPosicaoZ() {
        return posicaoZ;
    }

    public void setPosicaoZ(double posicaoZ) {
        this.posicaoZ = posicaoZ;
    }
}
