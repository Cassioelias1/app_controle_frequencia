package com.example.beacon.utils;

import org.altbeacon.beacon.Beacon;

import static java.lang.Math.pow;

public class BeaconUtils {

    //verificar se esse método funciona
    public static double calculateBeaconDistance(Beacon beacon) {
        double txPower = -74.0; // Manufacture set this power in the device
        if (beacon.getRssi() == 0){

            return -1.0; // if we cannot determine accuracy, return -1.
        }

        double ratio = beacon.getRssi() *1.0 / txPower;
        if (ratio < 1.0){
            return pow(ratio,10);

        }
        else{
            return (0.89976)*pow(ratio,7.7095) + 0.111;
        }
    }

    //Entender o que esse método faz
    private void getMeetingPoints(double distanceA, double distanceB, double distanceC, double pointA1, double pointA2, double pointB1, double pointB2, double pointC1, double pointC2) {
        double w,z,x,y,y2;
        w = distanceA * distanceA - distanceB * distanceB - pointA1 * pointA1 - pointA2* pointA2 + pointB1 * pointB1 + pointB2 * pointB2;

        z = distanceB * distanceB - distanceC * distanceC - pointB1* pointB1 - pointB2 * pointB2 + pointC1 * pointC1 + pointC2 * pointC2;

        x = (w * ( pointC2 - pointB2) - z * ( pointB2 - pointA2)) / (2 * (( pointB1 - pointA1) * ( pointC1 - pointB2) - ( pointC1 - pointB1) * ( pointB2 - pointA2)));

        y = (w - 2 * x * (pointB1 - pointA1)) / (2 * ( pointB2 - pointA2));

        y2 = (z - 2 * x * ( pointC1 -pointB1)) / (2 * ( pointC1 - pointB2));

        y = (y + y2) / 2;
    }
}
