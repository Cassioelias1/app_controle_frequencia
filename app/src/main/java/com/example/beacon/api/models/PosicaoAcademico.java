package com.example.beacon.api.models;

public class PosicaoAcademico {

    private double posicaoX;//Referente ao plano zy / Beacon2
    private double posicaoY;//Referente ao plano xz / Beacon1
    private double posicaoZ;//Referente ao plano xy / Beacon3
    private String status;

    public PosicaoAcademico() {
    }

    public PosicaoAcademico(double posicaoX, double posicaoY, double posicaoZ) {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
