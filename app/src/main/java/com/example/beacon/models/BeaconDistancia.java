package com.example.beacon.models;

import java.math.BigDecimal;

public class BeaconDistancia {
    private BigDecimal distancia;
    private Beacon beacon;

    public BeaconDistancia(BigDecimal distancia, Beacon beacon) {
        this.distancia = distancia;
        this.beacon = beacon;
    }

    public BigDecimal getDistancia() {
        return distancia;
    }

    public void setDistancia(BigDecimal distancia) {
        this.distancia = distancia;
    }

    public Beacon getBeacon() {
        return beacon;
    }

    public void setBeacon(Beacon beacon) {
        this.beacon = beacon;
    }
}
