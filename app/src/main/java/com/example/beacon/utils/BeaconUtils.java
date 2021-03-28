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

    public static double calcularDistanciaByRssi(List<Integer> ultimosSeisRssis, Context context) {
        double txPower = -63;
        int rssi = calcularMediaRssi(ultimosSeisRssis);

        if(rssi == 0){
            return -1;
        }

        double ratio = rssi * 1.0 / txPower;

        if(ratio < 1.0){
            double a = Math.pow(ratio, 10);
            teste(a, rssi, context);
            return a;
        } else {
            double b = 1.203420305 * Math.pow(ratio, 6.170094565) + 0.059805905;
            teste(b, rssi, context);
            return b;
        }
    }

    private static void teste(double distance, int mediaRssi, Context context){
        if (distance > 0){
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Util.sendNotification("result", distance+" | "+mediaRssi, notificationManager, context);
        }
    }
}
