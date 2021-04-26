package com.example.beacon.services;

import android.content.Context;

import com.example.beacon.api.models.PosicaoAcademico;
import com.example.beacon.context.AppContext;
import com.example.beacon.models.Beacon;
import com.example.beacon.models.BeaconDistancia;
import com.example.beacon.models.ContantesDistancia;
import com.example.beacon.utils.Shared;
import com.example.beacon.utils.Util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BeaconService {
    private static boolean COMAJUSTECONSTANTES = false;

    public static BeaconService instance(){
        return new BeaconService();
    }

    //este método deve ser responsavel por aplicar a trilateração com 4 beacons -> https://eg.uc.pt/bitstream/10316/31758/1/Tese_AnaRitaPereira.pdf
    public PosicaoAcademico academicoEstaDentroSalaAula(Map<String, Integer> beaconMediaRssiMap, Context context){
        if(beaconMediaRssiMap.size() < 4){
            return null;//se não está captando pelo menos 4 beacons significa que o academico não está em sala de aula.
        }

        //Dados total da sala
        //Altura (Z) = 2.60
        //Largura Lateral (X) = 4.55
        //Largura Frontal (Y) = 3.70

        //b1 = (0, 0, 0) -> coordenadas do beacon 1
        //b2 = (4.55, 0, 0) -> coordenadas do beacon 2
        //b3 = (0, 3.70, 0) -> coordenadas do beacon 3
        //b4 = (2.275, 1.85, 2.60) -> coordenadas do beacon 4

        BigDecimal distanciaBeacon1 = BigDecimal.valueOf(calcularDistanciaByRssi(beaconMediaRssiMap.get(Shared.getString(context,"id_beacon_1"))));
        BigDecimal distanciaBeacon2 = BigDecimal.valueOf(calcularDistanciaByRssi(beaconMediaRssiMap.get(Shared.getString(context,"id_beacon_2"))));
        BigDecimal distanciaBeacon3 = BigDecimal.valueOf(calcularDistanciaByRssi(beaconMediaRssiMap.get(Shared.getString(context,"id_beacon_3"))));
        BigDecimal distanciaBeacon4 = BigDecimal.valueOf(calcularDistanciaByRssi(beaconMediaRssiMap.get(Shared.getString(context,"id_beacon_4"))));

        BigDecimal medidaLadoX = new BigDecimal(Shared.getString(context, "medida_lado_x"));
        BigDecimal medidaLadoY = new BigDecimal(Shared.getString(context, "medida_lado_y"));
        BigDecimal medidaLadoZ = new BigDecimal(Shared.getString(context, "medida_lado_z"));

        //TODO: Verificar uma estratégia para caso o academico não tenha internet.
        //podem ser nulos se houver erro na requisição para recuperar a aula do dia do academico ou se for sabádo ou domingo.
        if (medidaLadoX == null || medidaLadoY == null || medidaLadoZ == null){
            return null;
        }

        //Podem ser nulos se o acadêmico não estiver perto da sala de aula, onde o app não consiga fazer a leitura dos beacons.
        if (distanciaBeacon1 == null || distanciaBeacon2 == null || distanciaBeacon3 == null || distanciaBeacon4 == null) {
            return null;
        }

        BigDecimal ZERO = BigDecimal.ZERO;

        Beacon beacon1 = new Beacon(ZERO, ZERO, ZERO);
        Beacon beacon2 = new Beacon(medidaLadoX, ZERO, ZERO);
        Beacon beacon3 = new Beacon(ZERO, medidaLadoY, ZERO);
        Beacon beacon4 = new Beacon(medidaLadoX.divide(Util.DOIS, RoundingMode.HALF_UP), medidaLadoY.divide(Util.DOIS, RoundingMode.HALF_UP), medidaLadoZ);

        BeaconDistancia beaconDistancia1 = new BeaconDistancia(distanciaBeacon1, beacon1);
        BeaconDistancia beaconDistancia2 = new BeaconDistancia(distanciaBeacon2, beacon2);
        BeaconDistancia beaconDistancia3 = new BeaconDistancia(distanciaBeacon3, beacon3);
        BeaconDistancia beaconDistancia4 = new BeaconDistancia(distanciaBeacon4, beacon4);

        BigDecimal posicaoXAcademico = getPosicaoX(beaconDistancia1, beaconDistancia2);
        BigDecimal posicaoYAcademico = getPosicaoY(beaconDistancia1, beaconDistancia3, posicaoXAcademico);
        BigDecimal posicaoZAcademico = getPosicaoZ(beaconDistancia1, beaconDistancia4, posicaoXAcademico, posicaoYAcademico);

        PosicaoAcademico posicaoAcademico = new PosicaoAcademico(posicaoXAcademico, posicaoYAcademico, posicaoZAcademico);

        //Caso algumas das posições calculadas seja maior que o total da sala.
        if (posicaoAcademico.getPosicaoX().compareTo(medidaLadoX) > 0 || posicaoAcademico.getPosicaoY().compareTo(medidaLadoY) > 0 || posicaoAcademico.getPosicaoZ().compareTo(medidaLadoZ) > 0) {
            posicaoAcademico.falta();
            return posicaoAcademico;
        }

        return posicaoAcademico.presenca();
    }

    private BigDecimal getPosicaoX(BeaconDistancia beaconDistancia1, BeaconDistancia beaconDistancia2){
        BigDecimal distancia1AoQuadrado = beaconDistancia1.getDistancia().pow(2);
        BigDecimal distancia2AoQuadrado = beaconDistancia2.getDistancia().pow(2);
        BigDecimal posicaoXBeacon2 = beaconDistancia2.getBeacon().getPosicaoX();
        BigDecimal posicaoXBeacon2AoQuadrado = posicaoXBeacon2.pow(2);

        return distancia1AoQuadrado
                .subtract(distancia2AoQuadrado)
                .add(posicaoXBeacon2AoQuadrado)
                .divide(Util.DOIS.multiply(posicaoXBeacon2), RoundingMode.HALF_UP);
    }

    private BigDecimal getPosicaoY(BeaconDistancia beaconDistancia1, BeaconDistancia beaconDistancia3, BigDecimal posicaoXAcademico){
        BigDecimal distancia1AoQuadrado = beaconDistancia1.getDistancia().pow(2);
        BigDecimal distancia3AoQuadrado = beaconDistancia3.getDistancia().pow(2);
        BigDecimal posicaoXBeacon3 = beaconDistancia3.getBeacon().getPosicaoX();
        BigDecimal posicaoXBeacon3AoQuadrado = posicaoXBeacon3.pow(2);
        BigDecimal posicaoYBeacon3 = beaconDistancia3.getBeacon().getPosicaoY();
        BigDecimal posicaoYBeacon3AoQuadrado = posicaoYBeacon3.pow(2);

        return distancia1AoQuadrado
                .subtract(distancia3AoQuadrado)
                .add(posicaoXBeacon3AoQuadrado)
                .add(posicaoYBeacon3AoQuadrado)
                .subtract(Util.DOIS.multiply(posicaoXBeacon3).multiply(posicaoXAcademico))
                .divide(Util.DOIS.multiply(posicaoYBeacon3), RoundingMode.HALF_UP);
    }

    private BigDecimal getPosicaoZ(BeaconDistancia beaconDistancia1, BeaconDistancia beaconDistancia4, BigDecimal posicaoXAcademico, BigDecimal posicaoYAcademico){
        BigDecimal distancia1AoQuadrado = beaconDistancia1.getDistancia().pow(2);
        BigDecimal distancia4AoQuadrado = beaconDistancia4.getDistancia().pow(2);
        BigDecimal posicaoXBeacon4 = beaconDistancia4.getBeacon().getPosicaoX();
        BigDecimal posicaoXBeacon4AoQuadrado = posicaoXBeacon4.pow(2);
        BigDecimal posicaoYBeacon4 = beaconDistancia4.getBeacon().getPosicaoY();
        BigDecimal posicaoYBeacon4AoQuadrado = posicaoYBeacon4.pow(2);
        BigDecimal posicaoZBeacon4 = beaconDistancia4.getBeacon().getPosicaoZ();
        BigDecimal posicaoZBeacon4AoQuadrado = posicaoZBeacon4.pow(2);

        return distancia1AoQuadrado.subtract(distancia4AoQuadrado)
                .add(posicaoXBeacon4AoQuadrado)
                .add(posicaoYBeacon4AoQuadrado)
                .add(posicaoZBeacon4AoQuadrado)
                .subtract(Util.DOIS.multiply(posicaoXBeacon4).multiply(posicaoXAcademico))
                .subtract(Util.DOIS.multiply(posicaoYBeacon4).multiply(posicaoYAcademico))
                .divide(Util.DOIS.multiply(posicaoZBeacon4), RoundingMode.HALF_UP);
    }

    private Map<Integer, ContantesDistancia> createMap(){
        Map<Integer, ContantesDistancia> contantesMap = new HashMap<>();
        contantesMap.put(-59, new ContantesDistancia(-59,1.20090834192006, 3.85094187965639, -0.2009083419));//1m
        contantesMap.put(-79, new ContantesDistancia(-79,1.20090834192006, 3.85094187965639, -0.2009083419));//4m
        contantesMap.put(-88, new ContantesDistancia(-88,1.20090834192006, 3.85094187965639, -0.2009083419));
        return contantesMap;
    }

    private int getValorAbsoluto(Integer valor){
        return Math.abs(valor);
    }

    private int getDiferenciaValores(int valor1, int valor2){
        return valor1 - valor2;
    }

    private ContantesDistancia getConstantesByRssi(int medianaRssi){
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
                    //posicaoAtual = -62
                    //proximaPosicao = -68
                    //mediana = -63
                    int diferencaIndice = getValorAbsoluto(getDiferenciaValores(posicaoAtualAbsoluta, medianaAbsoluta));
                    int diferenciaProxIndice = getValorAbsoluto(getDiferenciaValores(proximaPosicaoAbsoluta, medianaAbsoluta));

                    if (diferencaIndice <= diferenciaProxIndice){
                        return contantesMap.get(keysRssis.get(i));
                    } else {
                        return contantesMap.get(keysRssis.get(proxIndice));
                    }
                }
            }
        }

        return null;
    }

    public double calcularDistanciaByRssi(int mediaRssi) {
        double rssiBaseUmMetro = -59;//TODO: Esse txPower é na verdade o RSSI médido a tal distância no "Iphone"

        if(mediaRssi == 0){
            return -1;
        }

        double A, B, C;
        if (COMAJUSTECONSTANTES) {
            ContantesDistancia constantesDistancia = getConstantesByRssi(mediaRssi);
            if (constantesDistancia == null) {
                return -1;
            }

            A = constantesDistancia.getConstanteA();
            B = constantesDistancia.getConstanteB();
            C = constantesDistancia.getConstanteC();
            rssiBaseUmMetro = constantesDistancia.getRssiUmMetro();
        } else {
            A = 1.20090834192006;
            B = 3.85094187965639;
            C = -0.2009083419;
        }

        double razao = mediaRssi * 1.0 / rssiBaseUmMetro;
        return A * Math.pow(razao, B) + C;
    }

    public int calcularMediaRssi(List<Integer> ultimosSeisRssis){
        int sum = ultimosSeisRssis.stream().reduce(0, Integer::sum);

        //ultimosSeisRssis sempre deverá ter pelo menos um item.
        return sum / ultimosSeisRssis.size();
    }

    public boolean rssiIsOutlier(int mediaRssi, int rssiLeituraAtual){
        if (mediaRssi == 0){
            return false;
        }
        rssiLeituraAtual = getValorAbsoluto(rssiLeituraAtual);
        mediaRssi = getValorAbsoluto(mediaRssi);

        int percentualAumento = ((rssiLeituraAtual - mediaRssi) / mediaRssi) * 100;

        return percentualAumento > 10;
    }
}
