package com.example.beacon.utils;

import com.example.beacon.models.ContantesDistancia;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BeaconUtils {
    public static BeaconUtils Instance(){
        return new BeaconUtils();
    }
    public BeaconUtils() {
    }

    private Map<Integer, ContantesDistancia> createMap(){
        Map<Integer, ContantesDistancia> contantesMap = new HashMap<>();
        contantesMap.put(-30, new ContantesDistancia(-30, 0.89976, 7.7095, 0.111));
        contantesMap.put(-32, new ContantesDistancia(-32, 0.89976, 7.7095, 0.111));
        contantesMap.put(-38, new ContantesDistancia(-38, 0.89976, 7.7095, 0.111));
        contantesMap.put(-40, new ContantesDistancia(-40, 0.89976, 7.7095, 0.111));
        contantesMap.put(-42, new ContantesDistancia(-42, 0.89976, 7.7095, 0.111));
        contantesMap.put(-48, new ContantesDistancia(-48, 0.89976, 7.7095, 0.111));
        contantesMap.put(-50, new ContantesDistancia(-50, 0.89976, 7.7095, 0.111));
        contantesMap.put(-52, new ContantesDistancia(-52, 0.89976, 7.7095, 0.111));
        contantesMap.put(-58, new ContantesDistancia(-58, 0.89976, 7.7095, 0.111));
        contantesMap.put(-60, new ContantesDistancia(-60, 0.89976, 7.7095, 0.111));
        contantesMap.put(-62, new ContantesDistancia(-62, 0.89976, 7.7095, 0.111));
        contantesMap.put(-68, new ContantesDistancia(-68, 0.89976, 7.7095, 0.111));
        contantesMap.put(-70, new ContantesDistancia(-70, 0.89976, 7.7095, 0.111));
        contantesMap.put(-72, new ContantesDistancia(-72, 0.89976, 7.7095, 0.111));
        contantesMap.put(-78, new ContantesDistancia(-78, 0.89976, 7.7095, 0.111));
        contantesMap.put(-80, new ContantesDistancia(-80, 0.89976, 7.7095, 0.111));
        contantesMap.put(-82, new ContantesDistancia(-82, 0.89976, 7.7095, 0.111));
        contantesMap.put(-88, new ContantesDistancia(-88, 0.89976, 7.7095, 0.111));
        contantesMap.put(-90, new ContantesDistancia(-90, 0.89976, 7.7095, 0.111));
        contantesMap.put(-92, new ContantesDistancia(-92, 0.89976, 7.7095, 0.111));
        contantesMap.put(-98, new ContantesDistancia(-98, 0.89976, 7.7095, 0.111));
        return contantesMap;
    }

    private int getValorAbsoluto(Integer valor){
        return Math.abs(valor);
    }

    private int getDiferenciaValores(int valor1, int valor2){
        return valor1 - valor2;
    }

    private ContantesDistancia getConstantesByRssi(int medianaRssi){
        ContantesDistancia constantesRetorno = null;
        Map<Integer, ContantesDistancia> contantesMap = createMap();
        List<Integer> keysRssis = contantesMap.keySet().stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
        int size = keysRssis.size();

        for (int i = 0; i < size; i++) {
            int proxIndice = i+1;
            if (proxIndice < size){
                int medianaAbsoluta = getValorAbsoluto(medianaRssi);
                int posicaoAtualAbsoluta = getValorAbsoluto(keysRssis.get(i));
                int proximaPosicaoAbsoluta = getValorAbsoluto(keysRssis.get(proxIndice));
                if (medianaAbsoluta >= posicaoAtualAbsoluta && medianaAbsoluta <= proximaPosicaoAbsoluta){
                    int diferencaIndice = getDiferenciaValores(posicaoAtualAbsoluta, medianaAbsoluta);
                    int diferenciaProxIndice = getDiferenciaValores(proximaPosicaoAbsoluta, medianaAbsoluta);

                    if (diferencaIndice < diferenciaProxIndice){
                        constantesRetorno = contantesMap.get(keysRssis.get(i));
                    } else {
                        constantesRetorno = contantesMap.get(keysRssis.get(proxIndice));
                    }
                    break;
                }
            }
        }

        return constantesRetorno;
    }

    public double calcularDistanciaByRssi(int mediaRssi) {
        double txPower = -65;

        if(mediaRssi == 0){
            return -1;
        }

        double ratio;
        boolean isEddystone = false;
        if (isEddystone){
            ratio = mediaRssi * 1.0 / (txPower - 36);
        } else {
            ratio = mediaRssi * 1.0 / txPower;
        }

        if(ratio < 1.0){
            return Math.pow(ratio, 10);
        } else {
            ContantesDistancia constantesDistancia = getConstantesByRssi(-94);

            double A = constantesDistancia.getConstanteA();
            double B = constantesDistancia.getConstanteB();
            double C = constantesDistancia.getConstanteC();

            return A * Math.pow(ratio, B) + C;
        }
    }

    public int calcularMediaRssi(List<Integer> ultimosSeisRssis){
        int sum = ultimosSeisRssis.stream().reduce(0, Integer::sum);

        //ultimosSeisRssis sempre deverá ter pelo menos um item.
        return sum / ultimosSeisRssis.size();
    }

    public boolean rssiIsOutlier(int mediaRssi, int rssiLeituraAtual){
        rssiLeituraAtual = getValorAbsoluto(rssiLeituraAtual);
        mediaRssi = getValorAbsoluto(mediaRssi);

        int percentualAumento = ((rssiLeituraAtual - mediaRssi) / mediaRssi) * 100;

        return percentualAumento > 10;
    }
}
