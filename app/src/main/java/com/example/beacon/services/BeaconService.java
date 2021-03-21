package com.example.beacon.services;

import com.example.beacon.api.models.PosicaoAcademico;
import com.example.beacon.context.AppContext;
import com.example.beacon.models.Beacon;
import com.example.beacon.models.BeaconDistancia;
import com.example.beacon.utils.Util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

public class BeaconService {

    public static BeaconService instance(){
        return new BeaconService();
    }

    //este método deve ser responsavel por aplicar a trilateração com 4 beacons -> https://eg.uc.pt/bitstream/10316/31758/1/Tese_AnaRitaPereira.pdf
    public PosicaoAcademico academicoEstaDentroSalaAula(Map<String, Double> beaconDistanceMap){
//        if(beaconDistanceMap.size() < 3){
        //return null;//se não está captando pelo menos 3 beacons significa que o academico não está em sala de aula.
//        }

        //Dados total da sala
        //Altura (Z) = 2.60
        //Largura Lateral (X) = 4.55
        //Largura Frontal (Y) = 3.70

        //b1 = (0, 0, 0) -> coordenadas do beacon 1
        //b2 = (4.55, 0, 0) -> coordenadas do beacon 2
        //b3 = (0, 3.70, 0) -> coordenadas do beacon 3
        //b4 = (2.275, 1.85, 2.60) -> coordenadas do beacon 4

        //a distancia até o beacon irá representa a posicação.
        Double distanciaBeacon1 = Util.getZeroIfNull(beaconDistanceMap.get(AppContext.getIdBeacon1()));
        Double distanciaBeacon2 = Util.getZeroIfNull(beaconDistanceMap.get(AppContext.getIdBeacon2()));
        Double distanciaBeacon3 = Util.getZeroIfNull(beaconDistanceMap.get(AppContext.getIdBeacon3()));
        Double distanciaBeacon4 = Util.getZeroIfNull(beaconDistanceMap.get(AppContext.getIdBeacon4()));

        Double medidaLadoX = AppContext.getMedidaLadoX();
        Double medidaLadoY = AppContext.getMedidaLadoY();
        Double medidaLadoZ = AppContext.getMedidaLadoZ();

        //podem ser nulos se houver erro na requisição para recuperar a aula do dia do academico ou se for sabádo ou domingo.
        if (medidaLadoX == null || medidaLadoY == null || medidaLadoZ == null){
            return null;
        }

        Beacon beacon1 = new Beacon(0, 0, 0);
        Beacon beacon2 = new Beacon(medidaLadoX, 0, 0);
        Beacon beacon3 = new Beacon(medidaLadoX, medidaLadoY, 0);
        Beacon beacon4 = new Beacon(medidaLadoX / 2, medidaLadoY / 2, medidaLadoZ);

        BeaconDistancia beaconDistancia1 = new BeaconDistancia(distanciaBeacon1, beacon1);
        BeaconDistancia beaconDistancia2 = new BeaconDistancia(distanciaBeacon2, beacon2);
        BeaconDistancia beaconDistancia3 = new BeaconDistancia(distanciaBeacon3, beacon3);
        BeaconDistancia beaconDistancia4 = new BeaconDistancia(distanciaBeacon4, beacon4);

        double posicaoXAcademico = getPosicaoX(beaconDistancia1, beaconDistancia2);
        double posicaoYAcademico = getPosicaoY(beaconDistancia1, beaconDistancia3, posicaoXAcademico);
        double posicaoZAcademico = getPosicaoZ(beaconDistancia1, beaconDistancia4, posicaoXAcademico, posicaoYAcademico);

        PosicaoAcademico posicaoAcademico = new PosicaoAcademico(posicaoXAcademico, posicaoYAcademico, posicaoZAcademico);

        //TODO: realizar algumas validação para garantir que essas posições estão dentro da sala de aula.

        return posicaoAcademico;
    }

    private Double getPosicaoX(BeaconDistancia beaconDistancia1, BeaconDistancia beaconDistancia2){
        double distancia1AoQuadrado = Math.pow(beaconDistancia1.getDistancia(), 2);
        double distancia2AoQuadrado = Math.pow(beaconDistancia2.getDistancia(), 2);
        double posicaoXBeacon2 = beaconDistancia2.getBeacon().getPosicaoX();
        double posicaoXBeacon2AoQuadrado = Math.pow(posicaoXBeacon2, 2);

        return (distancia1AoQuadrado - distancia2AoQuadrado + posicaoXBeacon2AoQuadrado) / (2 * posicaoXBeacon2);
    }

    private double getPosicaoY(BeaconDistancia beaconDistancia1, BeaconDistancia beaconDistancia3, double posicaoXAcademico){
        double distancia1AoQuadrado = Math.pow(beaconDistancia1.getDistancia(), 2);
        double distancia3AoQuadrado = Math.pow(beaconDistancia3.getDistancia(), 2);
        double posicaoXBeacon3 = beaconDistancia3.getBeacon().getPosicaoX();
        double posicaoXBeacon3AoQuadrado = Math.pow(posicaoXBeacon3, 2);
        double posicaoYBeacon3 = beaconDistancia3.getBeacon().getPosicaoY();
        double posicaoYBeacon3AoQuadrado = Math.pow(posicaoYBeacon3, 2);

        return (distancia1AoQuadrado - distancia3AoQuadrado + posicaoXBeacon3AoQuadrado + posicaoYBeacon3AoQuadrado) - (2 * posicaoXBeacon3 * posicaoXAcademico) / (2 * posicaoYBeacon3);
    }

    private double getPosicaoZ(BeaconDistancia beaconDistancia1, BeaconDistancia beaconDistancia4, double posicaoXAcademico, double posicaoYAcademico){
        double distancia1AoQuadrado = Math.pow(beaconDistancia1.getDistancia(), 2);
        double distancia4AoQuadrado = Math.pow(beaconDistancia4.getDistancia(), 2);
        double posicaoXBeacon4 = beaconDistancia4.getBeacon().getPosicaoX();
        double posicaoXBeacon4AoQuadrado = Math.pow(posicaoXBeacon4, 2);
        double posicaoYBeacon4 = beaconDistancia4.getBeacon().getPosicaoY();
        double posicaoYBeacon4AoQuadrado = Math.pow(posicaoYBeacon4, 2);
        double posicaoZBeacon4 = beaconDistancia4.getBeacon().getPosicaoZ();
        double posicaoZBeacon4AoQuadrado = Math.pow(posicaoZBeacon4, 2);

        return (distancia1AoQuadrado - distancia4AoQuadrado + posicaoXBeacon4AoQuadrado + posicaoYBeacon4AoQuadrado + posicaoZBeacon4AoQuadrado) - (2 * posicaoXAcademico * posicaoXAcademico) - (2 * posicaoYAcademico * posicaoYAcademico) / (2 * posicaoZBeacon4);
    }
}
