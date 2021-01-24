package com.example.beacon.utils;

import org.altbeacon.beacon.Beacon;

public class BeaconUtils {

    public static double calcularDistanciaByRssi(Beacon beacon) {
        double txPower = -59;

        if(beacon.getRssi() == 0){
            return -1;
        }

        double ratio = beacon.getRssi() * 1.0 / txPower;

        if(ratio < 1.0){
            return Math.pow(ratio, 10);
        } else {
            return 0.89976 * Math.pow(ratio, 7.7095) + 0.111;
        }
    }
}
