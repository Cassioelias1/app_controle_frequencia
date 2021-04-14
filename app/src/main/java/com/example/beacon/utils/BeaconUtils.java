package com.example.beacon.utils;

import java.util.List;

public class BeaconUtils {

    private static int calcularMediaRssi(List<Integer> ultimosSeisRssis){
        int sum = ultimosSeisRssis.stream().reduce(0, Integer::sum);

        //ultimosSeisRssis sempre deverá ter pelo menos um item.
        return sum / ultimosSeisRssis.size();
    }

    public static double calcularDistanciaByRssi(List<Integer> ultimosSeisRssis, int txPowerBeacon) {
        double txPower = -65;
        int rssi = calcularMediaRssi(ultimosSeisRssis);

        if(rssi == 0){
            return -1;
        }

        double ratio;
        boolean isEddystone = false;
        if (isEddystone){
            ratio = rssi * 1.0 / (txPower - 36);
        } else {
            ratio = rssi * 1.0 / txPower;
        }

        if(ratio < 1.0){
            return Math.pow(ratio, 10);
        } else {
            return 0.89976 * Math.pow(ratio, 7.7095) + 0.111;
        }
    }
}
