package com.example.beacon.heron;

public class Heron {

    private double lado1;
    private double lado2;
    private double lado3;

    public Heron(double lado1, double lado2, double lado3) {
        this.lado1 = lado1;
        this.lado2 = lado2;
        this.lado3 = lado3;
    }

    private double calcularAreaByHeron(){
        double perimetro = (this.lado1 + this.lado2 + this.lado3) / 2;

        return Math.sqrt(perimetro * (perimetro - this.lado1) * (perimetro - this.lado2) * (perimetro - this.lado3));
    }

    public double calcularAlturaLado(double ladoSerCalculado){
        double area = calcularAreaByHeron();
        return area / (ladoSerCalculado / 2);
    }
}
