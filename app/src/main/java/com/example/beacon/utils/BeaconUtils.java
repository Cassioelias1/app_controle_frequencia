package com.example.beacon.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeaconUtils {
    private Map<Integer, BigDecimal> createMap(){
        Map<Integer, BigDecimal> contantesMap = new HashMap<>();
        contantesMap.put(-30, new BigDecimal("3.89976"));
        contantesMap.put(-40, new BigDecimal("2.14554"));
        contantesMap.put(-50, new BigDecimal("1.9894"));
        contantesMap.put(-60, new BigDecimal("0.89976"));
        contantesMap.put(-70, new BigDecimal("0.80000"));
        contantesMap.put(-80, new BigDecimal("0.64461"));
        contantesMap.put(-90, new BigDecimal("0.44387"));
        return contantesMap;
    }
    private BigDecimal getConstanteAByRssi(int medianaRssi){
        BigDecimal constanteRetorno = null;
        Map<Integer, BigDecimal> contantesMap = createMap();

        for (Map.Entry<Integer, BigDecimal> set : contantesMap.entrySet()) {
            Integer rssi = set.getKey();
            BigDecimal constante = set.getValue();
            if (false){//Verificar qual rssi é o mais proximo da medianaRssi
                constanteRetorno = constante;
                break;
            }
        }

        return constanteRetorno;
    }

    private BigDecimal getConstanteBByRssi(){
        return BigDecimal.ZERO;
    }

    private BigDecimal getConstanteCByRssi(){
        return BigDecimal.ZERO;
    }

    public static BigDecimal calcularDistanciaByRssi(List<Integer> ultimosSeisRssis, int txPowerBeacon) {
        BigDecimal txPower = new BigDecimal("-65");
        int rssi = calcularMediaRssi(ultimosSeisRssis);

        if(rssi == 0){
            return new BigDecimal("-1");
        }

        BigDecimal A = new BigDecimal("0.89976");
        BigDecimal B = new BigDecimal("7.7095");
        BigDecimal C = new BigDecimal("0.111");

        BigDecimal ratio;
        boolean isEddystone = false;
        if (isEddystone){
//            ratio = rssi * 1.0 / (txPower - 36);
            ratio = new BigDecimal(rssi).multiply(BigDecimal.ONE).divide(txPower.subtract(new BigDecimal("36")), RoundingMode.HALF_UP);
        } else {
//            ratio = rssi * 1.0 / txPower;
            ratio = new BigDecimal(rssi).multiply(BigDecimal.ONE).divide(txPower, BigDecimal.ROUND_HALF_UP);
        }

        if(ratio.compareTo(BigDecimal.ONE) < 0){//ratio < 1.0
//            return Math.pow(ratio, 10);
            return ratio.pow(10);
        } else {
//            return A * Math.pow(ratio, B) + C;
            return A.multiply(ratio.pow(B.intValue())).add(C);
        }
    }

    private static int calcularMediaRssi(List<Integer> ultimosSeisRssis){
        int sum = ultimosSeisRssis.stream().reduce(0, Integer::sum);

        //ultimosSeisRssis sempre deverá ter pelo menos um item.
        return sum / ultimosSeisRssis.size();
    }
}
