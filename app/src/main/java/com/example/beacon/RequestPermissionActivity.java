package com.example.beacon;

import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.beacon.api.API;
import com.example.beacon.api.models.PosicaoAcademico;
import com.example.beacon.api.models.Presenca;
import com.example.beacon.api.models.Turma;
import com.example.beacon.context.AppContext;
import com.example.beacon.heron.Heron;
import com.example.beacon.sqlite.BancoController;
import com.example.beacon.utils.BeaconUtils;
import com.example.beacon.utils.Util;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.powersave.BackgroundPowerSaver;
import org.altbeacon.beacon.startup.RegionBootstrap;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestPermissionActivity extends AppCompatActivity implements BeaconConsumer /*RangeNotifier*/ {
    //y = 3,70 | x= 4,55 | z = 2,60
    //0 é em metros o ponto x
    //0 é em metros o ponto y
    //2,6 é em metros o ponto z
    //370 / 2 = 185

    //Sabonete = 0x0077656c6c636f726573736407
    //Estrela = fda50693-a4e2-4fb1-afcf-c6eb07647825-5-6 // id2 = 5, id3 = 6
    //Quadrado = fda50693-a4e2-4fb1-afcf-c6eb07647825-1-2 // id2 = 1, id3 = 2

    private static final String ID_BEACON_POSICAO_2275_0_130 = "0x0077656c6c636f726573736407";
    private static final String ID_BEACON_POSICAO_0_185_130 = "fda50693-a4e2-4fb1-afcf-c6eb07647825-5-6";
    private static final String ID_BEACON_POSICAO_2275_185_260 = "fda50693-a4e2-4fb1-afcf-c6eb07647825-1-2";
    private Handler handler;

    protected final String TAG = RequestPermissionActivity.this.getClass().getSimpleName();
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    private static final int REQUEST_ENABLE_BLUETOOTH = 1;
    private static final long DEFAULT_SCAN_PERIOD_MS = 20000;
    private static final String ALL_BEACONS_REGION = "AllBeaconsRegion";
    private static final String I_BEACON = "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24";

    private BeaconManager mBeaconManager;
    private Region mRegion;

    private BackgroundPowerSaver backgroundPowerSaver;
    private RegionBootstrap regionBootstrap;
    private boolean teste = true;
    private Context context = this;

    private Map<String, Double> beaconDistanceMap = new HashMap<>();
    private BancoController controller;

    boolean[] validarPresencaDia = {true, true, true, true};
    boolean[] resetarCardsPresencas = {true};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        initBottomNavigation();
        handler = new Handler();
//        Executors.newSingleThreadExecutor();

        mBeaconManager = BeaconManager.getInstanceForApplication(this);
//        mBeaconManager = org.altbeacon.beacon.BeaconManager.getInstanceForApplication(this);

        List<BeaconParser> beaconParsers = Arrays.asList(new BeaconParser().setBeaconLayout(BeaconParser.EDDYSTONE_UID_LAYOUT),
                new BeaconParser().setBeaconLayout(BeaconParser.URI_BEACON_LAYOUT), new BeaconParser().setBeaconLayout(BeaconParser.ALTBEACON_LAYOUT),
                new BeaconParser().setBeaconLayout(BeaconParser.EDDYSTONE_TLM_LAYOUT), new BeaconParser().setBeaconLayout(BeaconParser.EDDYSTONE_URL_LAYOUT),
                new BeaconParser().setBeaconLayout(I_BEACON));
        mBeaconManager.getBeaconParsers().addAll(beaconParsers);

        ArrayList<Identifier> identifiers = new ArrayList<>();

        mRegion = new Region(ALL_BEACONS_REGION, identifiers);

        backgroundPowerSaver = new BackgroundPowerSaver(this);

        MaterialCardView materialCardView1915 = findViewById(R.id.card_19_15);
        TextView textView1915 = findViewById(R.id.textView1915);

        MaterialCardView materialCardView2015 = findViewById(R.id.card_20_15);
        TextView textView2015 = findViewById(R.id.textView2015);

        MaterialCardView materialCardView2100 = findViewById(R.id.card_21_00);
        TextView textView2100 = findViewById(R.id.textView2100);

        MaterialCardView materialCardView2140 = findViewById(R.id.card_21_40);
        TextView textView2140 = findViewById(R.id.textView2140);

//        SegundoPlano segundoPlano1915 = new SegundoPlano(materialCardView1915, textView1915, 23, 0, AppContext.getThread1915(), "card_19_15", "textView1915", handler, context);
//        segundoPlano1915.execute();

//        SegundoPlano segundoPlano2015 = new SegundoPlano(materialCardView2015, textView2015, 23, 27, AppContext.getThread2015(), "card_20_15", "textView2015", handler, context);
//        segundoPlano2015.execute();

//        SegundoPlano segundoPlano2100 = new SegundoPlano(materialCardView2100, textView2100, 23, 28, AppContext.getThread2100(), "card_21_00", "textView2100", handler, context);
//        segundoPlano2100.execute();

//        SegundoPlano segundoPlano2140 = new SegundoPlano(materialCardView2140, textView2140, 23, 29, AppContext.getThread2140(), "card_21_40", "textView2140", handler, context);
//        segundoPlano2140.execute();

        onInitThread(materialCardView1915, textView1915, 22, 0, AppContext.getThread1915(), "card_19_15", "textView1915", 0);
        onInitThread(materialCardView2015, textView2015, 22, 1, AppContext.getThread2015(), "card_20_15", "textView2015", 1);
        onInitThread(materialCardView2100, textView2100, 22, 2, AppContext.getThread2100(), "card_21_00", "textView2100", 2);
        onInitThread(materialCardView2140, textView2140, 22, 8, AppContext.getThread2140(), "card_21_40", "textView2140", 3);

        TextView textViewNomeDisciplica = findViewById(R.id.nomeDisciplinaHoje);
        textViewNomeDisciplica.setText(AppContext.getNomeTurma());
//
//        List<MaterialCardView> materialCardViews = new ArrayList<>();
//        materialCardViews.add(materialCardView1915);
//        materialCardViews.add(materialCardView2015);
//        materialCardViews.add(materialCardView2100);
//        materialCardViews.add(materialCardView2140);
//
//        List<TextView> textViews = new ArrayList<>();
//        textViews.add(textView1915);
//        textViews.add(textView2015);
//        textViews.add(textView2100);
//        textViews.add(textView2140);

//        resetCardsPresencas(materialCardViews, textViews);

//        createAlarmManagerToBackgroundProcess(0, 5);
        initServiceFindBeacons();
        getAulaDiaAcademico();
        getPresencasJaValidadas();
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
                    case R.id.sincronizacao:
                        startActivity(new Intent(getApplicationContext(), SincronizacaoActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
    }

    private void createAlarmManagerToBackgroundProcess(Integer hora, Integer minuto, String nameCard, String nameTextView){
        Intent intent = new Intent(context, AlarmPresencas.class);
        intent.putExtra("nameCard", nameCard);
        intent.putExtra("nameTextView", nameTextView);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hora);
        calendar.set(Calendar.MINUTE, minuto);
        calendar.set(Calendar.SECOND, 20);//20sec para dar tempo de validar a presença quando o app estiver aberto.

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    private void onInitThread(final MaterialCardView materialCardView, final TextView textView, final Integer hour, final Integer minute, Thread thread, String nameMaterialCard, String nameTextView, int posicaoThread) {
        if (thread == null){
            thread = new Thread() {
                @Override
                public void run() {
                    //createAlarmManagerToBackgroundProcess(hour, minute, nameMaterialCard, nameTextView);
                    while (validarPresencaDia[posicaoThread]) {
                        try {
                            LocalDateTime agora = LocalDateTime.now();
                            LocalDateTime primeiroHorario = LocalDateTime.of(agora.getYear(), agora.getMonth(), agora.getDayOfMonth(), hour, minute);
                            if (agora.getHour() == primeiroHorario.getHour() && agora.getMinute() == primeiroHorario.getMinute()){
                                validarPresencaDia[posicaoThread] = false;

                                if (posicaoThread == 3){//No momento em que for validada a ultima presença do academico, iniciar esta variavel para que o app realize o reset a meia noite.
                                    resetarCardsPresencas[0] = true;
                                }

                                Presenca presenca = new Presenca(AppContext.getAcademicoId(), AppContext.getTurmaId(), LocalDateTime.now().toString(), nameMaterialCard, nameTextView);
                                presenca.setPosicaoAcademicoHorarioAulaAndSetStatus(academicoEstaDentroSalaAula());
                                validarPresencaApi(presenca, materialCardView, textView, nameMaterialCard, nameTextView);
                            }
                        } catch (Exception e) {
                        }
                    }
                }
            };

            switch (nameMaterialCard) {
                case "card_19_15":
                    AppContext.setThread1915(thread);
                    break;
                case "card_20_15":
                    AppContext.setThread2015(thread);
                    break;
                case "card_21_00":
                    AppContext.setThread2100(thread);
                    break;
                case "card_21_40":
                    AppContext.setThread2140(thread);
                    break;
            }

            if (!thread.isAlive()){
                thread.start();
            }
        }
    }

    //este método deve ser responsavel por aplicar a trilateração
    private PosicaoAcademico academicoEstaDentroSalaAula(){
//        if(beaconDistanceMap.size() < 3){
            //return null;//se não está captando pelo menos 3 beacons significa que o academico não está em sala de aula.
//        }

        //Dados total da sala
        //Altura (Z) = 2.60
        //Largura Lateral (X) = 4.55
        //Largura Frontal (Y) = 3.70

        //a distancia até o beacon irá representa a posicação.
        Double distanciaBeacon1 = beaconDistanceMap.get(AppContext.getIdBeacon1());
        Double distanciaBeacon2 = beaconDistanceMap.get(AppContext.getIdBeacon2());
        Double distanciaBeacon3 = beaconDistanceMap.get(AppContext.getIdBeacon3());
        Double distanciaBeacon4 = beaconDistanceMap.get(AppContext.getIdBeacon4());

        if (distanciaBeacon1 == null || distanciaBeacon2 == null || distanciaBeacon3 == null || distanciaBeacon4 == null){
            return null;
        }

//        distanciaBeacon2 = 5.5;
//        distanciaBeacon3 = 6;

        //TODO: Posição x será calculada a partir dos beacons 2 e 3 juntamente ao tamanho do lado x
        Heron heronX = new Heron(distanciaBeacon2, distanciaBeacon3, 4.55);//o lado 3 irá representar a medida do lado da sala.
        //TODO: Posição y será calculada a partir dos beacons 2 e 4 juntamente ao tamanho do lado y
        Heron heronY = new Heron(distanciaBeacon2, distanciaBeacon4, 3.70);//o lado 3 irá representar a medida do lado da sala.
        //TODO: Posição z será calculada a partir dos beacons 1 e 2 juntamente ao tamanho do lado z
        Heron heronZ = new Heron(distanciaBeacon1, distanciaBeacon2, 2.60);//o lado 3 irá representar a medida do lado da sala.


        PosicaoAcademico posicaoAcademico = new PosicaoAcademico(heronX.calcularAlturaLado(4.55), heronY.calcularAlturaLado(3.70), heronZ.calcularAlturaLado(2.60));

//        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        String result = "("+posicaoAcademico.getPosicaoX()+", "+posicaoAcademico.getPosicaoY()+", "+posicaoAcademico.getPosicaoZ()+")";
//        Util.sendNotification("result", result, notificationManager, context);

        //TODO: fazer algumas validações em posicaoAcademico para verificar se as posições calculadas estão dentro da sala de aula de fato.

        return posicaoAcademico;
        //String.format("%.2f", heron.calcularAlturaLado(5)); salvar assim no banco
    }

    private boolean isMaior(BigDecimal posicaoCalculada, BigDecimal posicaoMaxima){
        return posicaoCalculada.compareTo(posicaoMaxima) > 0;
    }

    private void validarPresencaApi(Presenca presenca, MaterialCardView materialCardView, TextView textView, String nameMaterialCard, String nameTextView){
        API.validarPresenca(presenca, new Callback<Presenca>() {
            @Override
            public void onResponse(Call<Presenca> call, Response<Presenca> response) {
                if (response.body() != null){
                    if (response.body().getMensagemRetorno() != null){
                        handler.post(() -> {
                            if ("PRESENTE".equals(response.body().getStatus())){
                                materialCardView.setBackgroundColor(Color.parseColor("#11A33F"));
                                textView.setText("Presença válidada!");
                            } else if("AUSENTE".equals(response.body().getStatus())) {
                                materialCardView.setBackgroundColor(Color.parseColor("#b00e29"));
                                textView.setText("Falta computada!");
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<Presenca> call, Throwable t) {
                savePresencaNaoComputada(presenca, nameMaterialCard, nameTextView);
            }
        });
    }

    private boolean savePresencaNaoComputada(Presenca presenca, String nameMaterialCard, String nameTextView){
        if (controller == null) {
            controller = new BancoController(context);
        }
        return controller.save(presenca.getData(), presenca.getIdAcademico(), presenca.getIdTurma(), presenca.getStatus(), nameMaterialCard, nameTextView);
    }

    //TODO: este método deve resetar a variavel validarPresencaDia e limpar os quadrados da tela principal.
    private void resetCardsPresencas(final List<MaterialCardView> materialCardViews, final List<TextView> textViews){
        new Thread(){
            @Override
            public void run() {
                try {
                    while (resetarCardsPresencas[0]) {
                        LocalDateTime agora = LocalDateTime.now();
                        //Irá resetar meia noite, por isso 00:00:00
                        if (agora.getHour() == 0 && agora.getMinute() == 0 && agora.getSecond() == 0) {
                            resetarCardsPresencas[0] = false;
                            validarPresencaDia[0] = true;
                            validarPresencaDia[1] = true;
                            validarPresencaDia[2] = true;
                            validarPresencaDia[3] = true;
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
    private void initServiceFindBeacons(){
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                askForLocationPermissions();
                //Implementar algo que depois que conceder a permissão o processo de localização dos beacon inicie, chamar a activity novamente?
            } else {
                prepareDetection();
            }
    }

    private void stopServiceFindBeacons(){
        stopDetectingBeacons();
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

//        if (mBluetoothAdapter.isEnabled()) {
//            mBluetoothAdapter.disable();
//        }
    }

    private void showToastMessage (String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBeaconManager.removeAllRangeNotifiers();
        mBeaconManager.unbind(this);

//        Intent intent = new Intent("RECEIVER_ALARM");
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
//        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//        alarmManager.cancel(pendingIntent);
    }

    private void askForLocationPermissions() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.location_access_needed);
        builder.setMessage(R.string.grant_location_access);
        builder.setPositiveButton(android.R.string.ok, null);
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            public void onDismiss(DialogInterface dialog) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        PERMISSION_REQUEST_COARSE_LOCATION);

                initServiceFindBeacons();
            }
        });
        builder.show();
    }

    private void stopDetectingBeacons() {

        try {
            mBeaconManager.stopMonitoringBeaconsInRegion(mRegion);
            showToastMessage(getString(R.string.stop_looking_for_beacons));
        } catch (RemoteException e) {
            Log.d(TAG, "Se ha producido una excepción al parar de buscar beacons " + e.getMessage());
        }

        mBeaconManager.removeAllRangeNotifiers();

        mBeaconManager.unbind(this);
    }

    private void prepareDetection() {
        if (!isLocationEnabled()) {
            askToTurnOnLocation();

        } else {
            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

            if (mBluetoothAdapter == null) {
                showToastMessage(getString(R.string.not_support_bluetooth_msg));

            } else if (mBluetoothAdapter.isEnabled()) {
                startDetectingBeacons();

            } else {
                // Pedindo para que o usuário ative o Bluetooth
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BLUETOOTH);
                //Implementar algo que depois que conceder a permissão o processo de localização dos beacon inicie
            }
        }
    }

    private boolean isLocationEnabled() {
        LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        boolean networkLocationEnabled = false;

        boolean gpsLocationEnabled = false;

        try {
            networkLocationEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            gpsLocationEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);

        } catch (Exception ex) {
            Log.d(TAG, "Excepción al obtener información de localización");
        }

        return networkLocationEnabled || gpsLocationEnabled;
    }

    private void askToTurnOnLocation() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage(R.string.location_disabled);
        dialog.setPositiveButton(R.string.location_settings, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                // TODO Auto-generated method stub
                Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(myIntent);
            }
        });
        dialog.show();
    }

    private void startDetectingBeacons() {
        mBeaconManager.setForegroundScanPeriod(DEFAULT_SCAN_PERIOD_MS);
        mBeaconManager.bind(this);
    }


    @Override
    protected void onPause() {
        super.onPause();
        mBeaconManager.unbind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBeaconManager.bind(this);
    }

    @Override
    public void onBeaconServiceConnect() {
        RangeNotifier rangeNotifier = (beacons, region) -> {
            if (beacons.size() > 0) {
//                    if (!beaconDistanceMap.isEmpty()){
//                        beaconDistanceMap.clear();
//                    }
                //verificar se isso vai zerar os registros antigos.
                beaconDistanceMap = new HashMap<>();

                String idFinal;
                String id1 = null;
                String id2 = null;
                String id3 = null;

                for (Beacon beacon : beacons) {
                    id1 = beacon.getId1() != null ? beacon.getId1().toString() : "";
                    if (beacon.getIdentifiers().size() > 3) {//Alguns beacons não possuem id2 e id3
                        id2 = beacon.getId2() != null ? beacon.getId2().toString() : "";
                        id3 = beacon.getId3() != null ? beacon.getId3().toString() : "";
                    }
                    //Utilizar os 3 ids, pois alguns beacons podem conter id1 iguais.
                    idFinal = id1 + "-" + Util.getEmptyIfNull(id2) + "-" + Util.getEmptyIfNull(id3);

                    Double distance = BeaconUtils.calcularDistanciaByRssi(beacon);
                    beaconDistanceMap.put(idFinal, distance);
                }
//                    NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//                    Util.sendNotification("result", result, notificationManager, context);
            } else {
                String msg = AppContext.getIdBeacon1() + " - " + AppContext.getIdBeacon2() + " - " + AppContext.getIdBeacon3();
                showToastMessage(msg);
            }
        };
        try {
            mBeaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
            mBeaconManager.addRangeNotifier(rangeNotifier);
//            mBeaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
//            mBeaconManager.addRangeNotifier(rangeNotifier);
        } catch (RemoteException e) {   }
    }

    //TODO: não da pra retornar essas informações no login
    private void getAulaDiaAcademico(){
        if (AppContext.getTurmaId() == null || AppContext.getNomeTurma() == null){
            API.getAulaDiaAcademico(new Callback<List<Turma>>() {
                @Override
                public void onResponse(Call<List<Turma>> call, Response<List<Turma>> response) {
                    List<Turma> turmas = response.body();
                    if (turmas != null && !turmas.isEmpty()) {
                        System.out.println("==================================");
                        System.out.println("teve resultado");
                        Turma turma = turmas.get(0);//A query vai retorna um resultado, foi tratado como list por conta de um erro relacionado a como o mysql retorna os valores
                        AppContext.setTurmaId(turma.getId().toString());
                        AppContext.setNomeTurma(turma.getDescricao());

                        AppContext.setIdBeacon1(turma.getIdBeacon1());
                        AppContext.setIdBeacon2(turma.getIdBeacon2());
                        AppContext.setIdBeacon3(turma.getIdBeacon3());
                        AppContext.setIdBeacon4(turma.getIdBeacon4());

                        TextView textViewNomeDisciplica = findViewById(R.id.nomeDisciplinaHoje);
                        textViewNomeDisciplica.setText(turma.getDescricao());
                    } else {
                        System.out.println("==================================");
                        System.out.println("sem resultado");
                        TextView textViewNomeDisciplica = findViewById(R.id.nomeDisciplinaHoje);
                        textViewNomeDisciplica.setText("Você não possui aula hoje");

                        //TODO: Apenas para testes, remover depois
                        AppContext.setTurmaId("1");
                    }
                }

                @Override
                public void onFailure(Call<List<Turma>> call, Throwable t) {
                }
            }, AppContext.getAcademicoId(), LocalDate.now().getDayOfWeek().toString());
        } else {
            TextView textViewNomeDisciplica = findViewById(R.id.nomeDisciplinaHoje);
            textViewNomeDisciplica.setText(AppContext.getNomeTurma());
        }
    }

    private void getPresencasJaValidadas(){
        API.getPresencaDiaAcademico(new Callback<List<Presenca>>() {
            @Override
            public void onResponse(Call<List<Presenca>> call, Response<List<Presenca>> response) {
                List<Presenca> presencas = response.body();

                if (presencas != null) {
                    for (Presenca presenca : presencas) {
                        String nameMaterialCard = presenca.getMaterialCardId();
                        String nameTextView = presenca.getTextViewId();
                        if (nameMaterialCard != null && nameTextView != null) {
                            int idMaterialCard = getResources().getIdentifier(nameMaterialCard, "id", context.getPackageName());
                            int idTextView = getResources().getIdentifier(nameTextView, "id", context.getPackageName());
                            MaterialCardView materialCardView = findViewById(idMaterialCard);
                            TextView textView = findViewById(idTextView);

                            if (materialCardView != null && textView != null) {
                                if ("PRESENTE".equals(presenca.getStatus())) {
                                    materialCardView.setBackgroundColor(Color.parseColor("#11A33F"));
                                    textView.setText("Presença válidada!");
                                } else if ("AUSENTE".equals(presenca.getStatus())) {
                                    materialCardView.setBackgroundColor(Color.parseColor("#b00e29"));
                                    textView.setText("Falta computada!");
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Presenca>> call, Throwable t) {
            }
        }, AppContext.getAcademicoId(), LocalDate.now().toString());
    }
}
