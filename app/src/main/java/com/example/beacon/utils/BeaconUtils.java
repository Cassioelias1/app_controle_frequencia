package com.example.beacon.utils;

import android.app.NotificationManager;
import android.content.Context;
import java.util.List;

public class BeaconUtils {

    private static int calcularMediaRssi(List<Integer> ultimosSeisRssis){
        int sum = ultimosSeisRssis.stream().reduce(0, Integer::sum);

        //ultimosSeisRssis sempre deverá ter pelo menos um item.
        return sum / ultimosSeisRssis.size();
    }

    public static double calcularDistanciaByRssi(List<Integer> ultimosSeisRssis) {
        double txPower = -63;
        int rssi = calcularMediaRssi(ultimosSeisRssis);

        if(rssi == 0){
            return -1;
        }

        double ratio = rssi * 1.0 / txPower;

        if(ratio < 1.0){
            return Math.pow(ratio, 10);
        } else {
            return 1.203420305 * Math.pow(ratio, 6.170094565) + 0.059805905;
        }
    }
}
