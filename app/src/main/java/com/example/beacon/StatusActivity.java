package com.example.beacon;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.beacon.api.API;
import com.example.beacon.api.models.Presenca;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatusActivity extends Activity implements BeaconConsumer {
    private static final String KEY_SIMULATE_BEACON = "5F469-D4GG-4AHA-SA5U0";
    private static final String ID_SIMULATE_ACADEMICO = "2";
    private Handler handler;

    private BeaconManager beaconManager = BeaconManager.getInstanceForApplication(this);

    private Set<Integer> identificadoresBeacons = new HashSet<>();
    private Thread thread = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        initBottomNavigation();
        handler = new Handler();

        List<HorarioValidacaoPresenca> horarioValidacaoPresencas = Arrays.asList(new HorarioValidacaoPresenca(19, 15),
                new HorarioValidacaoPresenca(20, 15), new HorarioValidacaoPresenca(21, 00), new HorarioValidacaoPresenca(21, 40));

        MaterialCardView materialCardView1915 = findViewById(R.id.card);
        TextView textView1915 = findViewById(R.id.textView1915);
//
//        MaterialCardView materialCardView2015 = findViewById(R.id.card2);
//        TextView textView2015 = findViewById(R.id.textView2015);
//
//        MaterialCardView materialCardView2100 = findViewById(R.id.card3);
//        TextView textView2100 = findViewById(R.id.textView2100);
//
//        MaterialCardView materialCardView2140 = findViewById(R.id.card4);
//        TextView textView2140 = findViewById(R.id.textView2140);

        List<MaterialCardView> materialCardViews = new ArrayList<>();
        materialCardViews.add(materialCardView1915);
//        materialCardViews.add(materialCardView2015);
//        materialCardViews.add(materialCardView2100);
//        materialCardViews.add(materialCardView2140);

        List<TextView> textViews = new ArrayList<>();
        textViews.add(textView1915);
//        textViews.add(textView2015);
//        textViews.add(textView2100);
//        textViews.add(textView2140);

        onInitThread(materialCardViews, textViews, horarioValidacaoPresencas);

//        resetCardsPresencas(materialCardViews, textViews, 23, 7);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        beaconManager.unbind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        beaconManager.bind(this);
    }

    @Override
    public void onBeaconServiceConnect() {
        RangeNotifier rangeNotifier = (beacons, region) -> {
            StringBuilder builder = new StringBuilder();
            if (beacons.size() > 0) {
//                builder.append("Quantidade de beacons localizados: ").append(beacons.size()).append("\n");
                for (Beacon beacon : beacons) {
                    double distancia = calculateDistance(beacon.getTxPower(), beacon.getRssi());
                    builder.append("ID: "+beacon.getId1()).append("\n");
                    builder.append("DISTANCIA 1: "+distancia).append("\n");
                    builder.append("DISTANCIA 2: "+beacon.getDistance()).append("\n");
                    builder.append("====================").append("\n");
                }


                showToastMessage(builder.toString());
            }
        };
        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
            beaconManager.addRangeNotifier(rangeNotifier);/*
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
            beaconManager.addRangeNotifier(rangeNotifier);*/
        } catch (RemoteException e) {   }
    }

    protected static double calculateDistance(int measuredPower, double rssi) {
        if (rssi == 0) {
            return -1.0; // if we cannot determine distance, return -1.
        }
        double ratio = rssi*1.0/measuredPower;
        if (ratio < 1.0) {
            return Math.pow(ratio,10);
        }
        else {
            double distance =  (0.89976)*Math.pow(ratio,7.7095) + 0.111;
            return distance;
        }
    }


    private void initBottomNavigation(){
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.status);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.status:
                        return true;
                    case R.id.aulas:
                        startActivity(new Intent(getApplicationContext(), AulasActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.presencas:
                        startActivity(new Intent(getApplicationContext(), PresencasActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
    }


    //Encaminhar uma lista com os horários e assim que passar de um horário (19:15 por exemplo) remove-lo da lista.
    //No final verificar se a lista está vazia, se sim fechar a thread.
    private void onInitThread(final List<MaterialCardView> materialCardViews, final List<TextView> textViews, List<HorarioValidacaoPresenca> horarioValidacaoPresencas) {
        List<HorarioValidacaoPresenca> horarioValidacoes = horarioValidacaoPresencas;
        final boolean[] presencaValidada = {false};
        thread = new Thread() {
            @Override
            public void run() {
                while (!presencaValidada[0]) {
                    try {
                        LocalDateTime agora = LocalDateTime.now();
                        for (HorarioValidacaoPresenca horarioValidacao : horarioValidacoes) {
                            LocalDateTime horarioPresenca = LocalDateTime.of(agora.getYear(), agora.getMonth(), agora.getDayOfMonth(), horarioValidacao.getHora(), horarioValidacao.getMinuto());
                            if (agora.getHour() == horarioPresenca.getHour() && agora.getMinute() == horarioPresenca.getMinute()){
                                String idsBeacons = identificadoresBeacons.stream().map(i -> i.toString().join(",")).toString();
                                //Se não tiver pelo menos 3 idsBeacon significa que o aluno não esta dentro da sala de aula, implementar outras validações (trilateração)
                                Presenca presenca = new Presenca(ID_SIMULATE_ACADEMICO, KEY_SIMULATE_BEACON, LocalDateTime.now().toString(), idsBeacons);
                                API.validarPresenca(presenca, new Callback<Presenca>() {
                                    @Override
                                    public void onResponse(Call<Presenca> call, Response<Presenca> response) {
                                        if (response.body() != null){
                                            if (response.body().getMensagemRetorno() != null){
                                                handler.post(new Runnable(){
                                                    @Override
                                                    public void run() {
                                                        materialCardViews.get(0).setBackgroundColor(Color.parseColor("#11A33F"));
                                                        if (!materialCardViews.isEmpty()){
                                                            materialCardViews.remove(materialCardViews.size() -1);
                                                        }
                                                        textViews.get(0).setText("Presença válidada!");
                                                        if (!textViews.isEmpty()){
                                                            textViews.remove(textViews.size() -1);
                                                        }

                                                        horarioValidacoes.remove(horarioValidacao);
                                                        validarThead(horarioValidacoes.size());
                                                    }
                                                });
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Presenca> call, Throwable t) {
                                        System.out.println("Erro na requisicao");
                                    }
                                });
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        startThreadValidacoes();
    }

    private void validarThead(int horariosPresencas){
        if (horariosPresencas == 0){
            thread.interrupt();
        }
    }

    private void startThreadValidacoes(){
        if (thread != null && !thread.isAlive()) {
            thread.start();
        }
    }

    private void resetCardsPresencas(final List<MaterialCardView> materialCardViews, final List<TextView> textViews, final Integer hour, final Integer minute){
        final boolean[] resetRealizado = {false};

        new Thread(){
            @Override
            public void run() {
                try {
                    while (!resetRealizado[0]) {
                        LocalDateTime agora = LocalDateTime.now();
                        LocalDateTime primeiroHorario = LocalDateTime.of(agora.getYear(), agora.getMonth(), agora.getDayOfMonth(), hour, minute);
                        if (agora.getHour() == primeiroHorario.getHour() && agora.getMinute() == primeiroHorario.getMinute()) {
                            resetRealizado[0] = true;
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    for (MaterialCardView materialCardView : materialCardViews) {
                                        materialCardView.setBackgroundColor(Color.parseColor("#E7731E"));
                                    }

                                    for (TextView textView : textViews) {
                                        textView.setText("Aguardando Horário");
                                    }
                                }
                            });

                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }.start();
    }


    private void showToastMessage (String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }


}
