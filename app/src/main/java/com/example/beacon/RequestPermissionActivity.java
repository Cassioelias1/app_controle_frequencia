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
import com.example.beacon.api.models.Presenca;
import com.example.beacon.api.models.Turma;
import com.example.beacon.context.AppContext;
import com.example.beacon.models.BeaconDistancia;
import com.example.beacon.services.BeaconService;
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

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
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

    //Celular com Beacon Simulator = a52d6b05-24ec-46b7-b3c1-cb853a6e20b2 TODO: Beacon1
    //Estrela = fda50693-a4e2-4fb1-afcf-c6eb0764782556 // id2 = 5, id3 = 6 TODO: Beacon2
    //Quadrado = fda50693-a4e2-4fb1-afcf-c6eb0764782512 // id2 = 1, id3 = 2 TODO: Beacon3
    //Sabonete = 0x0077656c6c636f726573736407 TODO: Beacon4
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
    private Context context = this;

    private Map<String, BigDecimal> beaconDistanceMap = new HashMap<>();
    private BancoController controller;

    boolean[] validarPresencaDia = {true, true, true, true};
    boolean[] resetarCardsPresencas = {true};

    private Map<String, List<Integer>> ultimosSeisRssisPorBeaconMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        initBottomNavigation();
        handler = new Handler();
        mBeaconManager = BeaconManager.getInstanceForApplication(this);

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

        onInitThread(materialCardView1915, textView1915, 13, 51, AppContext.getThread1915(), "card_19_15", "textView1915", 0);
        onInitThread(materialCardView2015, textView2015, 13, 28, AppContext.getThread2015(), "card_20_15", "textView2015", 1);
        onInitThread(materialCardView2100, textView2100, 13, 29, AppContext.getThread2100(), "card_21_00", "textView2100", 2);
        onInitThread(materialCardView2140, textView2140, 13, 30, AppContext.getThread2140(), "card_21_40", "textView2140", 3);

        TextView textViewNomeDisciplica = findViewById(R.id.nomeDisciplinaHoje);
        textViewNomeDisciplica.setText(AppContext.getNomeTurma());
//
        List<MaterialCardView> materialCardViews = new ArrayList<>();
        materialCardViews.add(materialCardView1915);
        materialCardViews.add(materialCardView2015);
        materialCardViews.add(materialCardView2100);
        materialCardViews.add(materialCardView2140);
//
        List<TextView> textViews = new ArrayList<>();
        textViews.add(textView1915);
        textViews.add(textView2015);
        textViews.add(textView2100);
        textViews.add(textView2140);

        resetCardsPresencas(materialCardViews, textViews);

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
                            if (agora.getDayOfWeek() != DayOfWeek.SATURDAY && agora.getDayOfWeek() != DayOfWeek.SUNDAY) {
                                if (agora.getHour() == primeiroHorario.getHour() && agora.getMinute() == primeiroHorario.getMinute()) {
                                    validarPresencaDia[posicaoThread] = false;

                                    if (posicaoThread == 3) {//No momento em que for validada a ultima presença do academico, iniciar esta variavel para que o app realize o reset a meia noite.
                                        resetarCardsPresencas[0] = true;
                                    }

                                    BeaconService beaconService = BeaconService.instance();
                                    Presenca presenca = new Presenca(AppContext.getAcademicoId(), AppContext.getTurmaId(), LocalDateTime.now().toString(), nameMaterialCard, nameTextView);
                                    presenca.setPosicaoAcademicoHorarioAulaAndSetStatus(beaconService.academicoEstaDentroSalaAula(beaconDistanceMap));
                                    validarPresencaApi(presenca, materialCardView, textView, nameMaterialCard, nameTextView);
                                }
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
//                if (!beaconDistanceMap.isEmpty()){
//                    beaconDistanceMap.clear();
//                }
                //verificar se isso vai zerar os registros antigos.
                beaconDistanceMap = new HashMap<>();

                String idFinal;
                String id1 = null;
                String id2 = null;
                String id3 = null;
                String distance2 = "";

                for (Beacon beacon : beacons) {
                    id1 = beacon.getId1() != null ? beacon.getId1().toString() : "";
                    if (beacon.getIdentifiers().size() > 3) {//Alguns beacons não possuem id2 e id3
                        id2 = beacon.getId2() != null ? beacon.getId2().toString() : "";
                        id3 = beacon.getId3() != null ? beacon.getId3().toString() : "";
                    }
                    //Utilizar os 3 ids, pois alguns beacons podem conter id1 iguais.
                    idFinal = id1 + Util.getEmptyIfNull(id2) + Util.getEmptyIfNull(id3);

                    List<Integer> ultimosSeisRssis = ultimosSeisRssisPorBeaconMap.get(idFinal);

                    if (ultimosSeisRssis == null){
                        ultimosSeisRssis = new ArrayList<>();
                        ultimosSeisRssis.add(beacon.getRssi());
                        ultimosSeisRssisPorBeaconMap.put(idFinal, ultimosSeisRssis);
                    } else if (ultimosSeisRssis.size() == 6){
                        ultimosSeisRssis.remove(0);//Remove o primeiro para substituir e sempre manter 6
                        ultimosSeisRssis.add(beacon.getRssi());
                    } else {
                        ultimosSeisRssis.add(beacon.getRssi());
                    }

                    double distance = BeaconUtils.calcularDistanciaByRssi(ultimosSeisRssis);
                    distance2 += distance + " - ";
                    beaconDistanceMap.put(idFinal, new BigDecimal(distance));
                }

                showToastMessage(distance2);
            }
        };
        try {
            mBeaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
            mBeaconManager.addRangeNotifier(rangeNotifier);
//            mBeaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
//            mBeaconManager.addRangeNotifier(rangeNotifier);
        } catch (RemoteException e) {   }
    }

    //TODO: Retornar as medidas da sala de aula aqui tbm.
    private void getAulaDiaAcademico(){
        if (AppContext.getTurmaId() == null || AppContext.getNomeTurma() == null){
            API.getAulaDiaAcademico(new Callback<List<Turma>>() {
                @Override
                public void onResponse(Call<List<Turma>> call, Response<List<Turma>> response) {
                    List<Turma> turmas = response.body();
                    if (turmas != null && !turmas.isEmpty()) {
                        Turma turma = turmas.get(0);//A query vai retorna um resultado, foi tratado como list por conta de um erro relacionado a como o mysql retorna os valores
                        AppContext.setTurmaId(turma.getId().toString());
                        AppContext.setNomeTurma(turma.getDescricao());

                        AppContext.setIdBeacon1(turma.getIdBeacon1());
                        AppContext.setIdBeacon2(turma.getIdBeacon2());
                        AppContext.setIdBeacon3(turma.getIdBeacon3());
                        AppContext.setIdBeacon4(turma.getIdBeacon4());

                        AppContext.setMedidaLadoX(new BigDecimal(turma.getMedidaLadoX()));
                        AppContext.setMedidaLadoY(new BigDecimal(turma.getMedidaLadoY()));
                        AppContext.setMedidaLadoZ(new BigDecimal(turma.getMedidaLadoZ()));

                        TextView textViewNomeDisciplica = findViewById(R.id.nomeDisciplinaHoje);
                        textViewNomeDisciplica.setText(turma.getDescricao());
                    } else {
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
