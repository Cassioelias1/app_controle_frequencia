package com.example.beacon;

public class HorarioValidacaoPresenca {
    Integer hora;
    Integer minuto;

    public HorarioValidacaoPresenca(Integer hora, Integer minuto) {
        this.hora = hora;
        this.minuto = minuto;
    }

    public Integer getHora() {
        return hora;
    }

    public void setHora(Integer hora) {
        this.hora = hora;
    }

    public Integer getMinuto() {
        return minuto;
    }

    public void setMinuto(Integer minuto) {
        this.minuto = minuto;
    }
}
