package com.example.beacon.models;

import java.math.BigDecimal;

public class BeaconDistancia {
    private double distancia;
    private Beacon beacon;

    public BeaconDistancia(double distancia, Beacon beacon) {
        this.distancia = distancia;
        this.beacon = beacon;
    }

    public double getDistancia() {
        return distancia;
    }

    public void setDistancia(double distancia) {
        this.distancia = distancia;
    }

    public Beacon getBeacon() {
        return beacon;
    }

    public void setBeacon(Beacon beacon) {
        this.beacon = beacon;
    }
}
