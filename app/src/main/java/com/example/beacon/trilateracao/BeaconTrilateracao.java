package com.example.beacon.trilateracao;

public class BeaconTrilateracao {

    private Double distance;
    private double rssi;

    public BeaconTrilateracao() {
    }

    public BeaconTrilateracao(Double distance, double rssi) {
        this.distance = distance;
        this.rssi = rssi;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public double getRssi() {
        return rssi;
    }

    public void setRssi(double rssi) {
        this.rssi = rssi;
    }
}
