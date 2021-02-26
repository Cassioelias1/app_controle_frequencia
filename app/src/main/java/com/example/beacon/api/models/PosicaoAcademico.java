package com.example.beacon.api.models;

import java.math.BigDecimal;

public class PosicaoAcademico {

    private BigDecimal posicaoX;//Referente ao plano zy / Beacon2
    private BigDecimal posicaoY;//Referente ao plano xz / Beacon1
    private BigDecimal posicaoZ;//Referente ao plano xy / Beacon3
    private String status;

    public PosicaoAcademico(BigDecimal posicaoX, BigDecimal posicaoY, BigDecimal posicaoZ) {
        this.posicaoX = posicaoX;
        this.posicaoY = posicaoY;
        this.posicaoZ = posicaoZ;
    }

    public PosicaoAcademico falta(){
        this.status = "AUSENTE";
        return this;
    }

    public PosicaoAcademico presenca(){
        this.status = "PRESENTE";
        return this;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
