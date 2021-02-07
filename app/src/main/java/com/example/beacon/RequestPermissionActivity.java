package com.example.beacon;

import android.Manifest;
import android.app.AlertDialog;
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
import com.example.beacon.sqlite.BancoController;
import com.example.beacon.utils.BeaconUtils;
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
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestPermissionActivity extends AppCompatActivity implements BeaconConsumer, RangeNotifier {
    //y = 3,70 | x= 4,55 | z = 2,60
    //0 é em metros o ponto x
    //0 é em metros o ponto y
    //2,6 é em metros o ponto z
    //370 / 2 = 185
    private static final String ID_BEACON_POSICAO_0_0_0 = "0x0077656c6c636f726573736407";
    private static final String ID_BEACON_POSICAO_0_370_0 = "2";
    private static final String ID_BEACON_POSICAO_455_185_260 = "3";
    private static final String ID_SIMULATE_ACADEMICO = "1";
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        initBottomNavigation();
        handler = new Handler();

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
//
        onInitThread(materialCardView1915, textView1915, 21, 35, AppContext.getThread1915(), "card_19_15", "textView1915");
        onInitThread(materialCardView2015, textView2015, 21, 36, AppContext.getThread2015(), "card_20_15", "textView2015");
        onInitThread(materialCardView2100, textView2100, 21, 37, AppContext.getThread2100(), "card_21_00", "textView2100");
        onInitThread(materialCardView2140, textView2140, 21, 38, AppContext.getThread2140(), "card_21_40", "textView2140");

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

//        resetCardsPresencas(materialCardViews, textViews, 23, 7);

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

    private void onInitThread(final MaterialCardView materialCardView, final TextView textView, final Integer hour, final Integer minute, Thread thread, String nameMaterialCard, String nameTextView) {
        final boolean[] presencaValidada = {false};

        if (thread == null){
            thread = new Thread() {
                @Override
                public void run() {
                    while (!presencaValidada[0]) {
                        try {
                            LocalDateTime agora = LocalDateTime.now();
                            LocalDateTime primeiroHorario = LocalDateTime.of(agora.getYear(), agora.getMonth(), agora.getDayOfMonth(), hour, minute);
                            if (agora.getHour() == primeiroHorario.getHour() && agora.getMinute() == primeiroHorario.getMinute()){

                                //Se não tiver pelo menos 3 idsBeacon significa que o aluno não esta dentro da sala de aula, implementar outras validações (trilateração)
                                //salvar turmaId no Context.
                                Presenca presenca = new Presenca(AppContext.getAcademicoId(), AppContext.getTurmaId(), LocalDateTime.now().toString(), nameMaterialCard, nameTextView);

                                presenca.setStatusTrilateracao(academicoEstaDentroSalaAula());
                                validarPresencaApi(presenca, materialCardView, textView);

                                presencaValidada[0] = true;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
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
    private boolean academicoEstaDentroSalaAula(){

        Map<String, Double> map = beaconDistanceMap;
        if(map.size() < 3){
            return false;//se não está captando pelo menos 3 beacons significa que o academico não está em sala de aula.
        }

        BigDecimal distanciaBeacon1 = null;
        BigDecimal distanciaBeacon2 = null;
        BigDecimal distanciaBeacon3 = null;

        //beacon 1 = (0,0)
        //beacon 2 = (0,3.7)
        //beacon 3 = (4.55,1.85)
        BigDecimal posicaoYBeacon2 = new BigDecimal("3.7");
        BigDecimal posicaoXBeacon3 = new BigDecimal("4.55");
        BigDecimal posicaoYBeacon3 = new BigDecimal("1.85");

        for (Map.Entry<String, Double> m : map.entrySet()) {
            if (m.getKey().equals(ID_BEACON_POSICAO_0_0_0)){
                distanciaBeacon1 = new BigDecimal(m.getValue());
            } else if(m.getKey().equals(ID_BEACON_POSICAO_0_370_0)){
                distanciaBeacon2 = new BigDecimal(m.getValue());
            } else if(m.getKey().equals(ID_BEACON_POSICAO_455_185_260)) {
                distanciaBeacon3 = new BigDecimal(m.getValue());
            }
        }

        if(distanciaBeacon1 == null || distanciaBeacon2 == null || distanciaBeacon3 == null){
            return false;
        }

        final BigDecimal DOIS = new BigDecimal("2");

        BigDecimal posX = distanciaBeacon1.pow(2).subtract(distanciaBeacon2.pow(2)).add(posicaoYBeacon2.pow(2))
                .divide(DOIS.multiply(posicaoYBeacon2), RoundingMode.HALF_UP);

        BigDecimal posY = distanciaBeacon1.pow(2).subtract(distanciaBeacon3.pow(2)).add(posicaoXBeacon3.pow(2)).add(posicaoYBeacon3.pow(2))
                .divide(DOIS.multiply(posicaoYBeacon3), RoundingMode.HALF_UP).subtract(posicaoXBeacon3.divide(posicaoYBeacon3, RoundingMode.HALF_UP)).multiply(posX);

        teste = !teste;
        return teste;
    }

    private void validarPresencaApi(Presenca presenca, MaterialCardView materialCardView, TextView textView){
        API.validarPresenca(presenca, new Callback<Presenca>() {
            @Override
            public void onResponse(Call<Presenca> call, Response<Presenca> response) {
                if (response.body() != null){
                    if (response.body().getMensagemRetorno() != null){
                        handler.post(new Runnable(){
                            @Override
                            public void run() {
                                if ("PRESENTE".equals(response.body().getStatus())){
                                    materialCardView.setBackgroundColor(Color.parseColor("#11A33F"));
                                    textView.setText("Presença válidada!");
                                } else if("AUSENTE".equals(response.body().getStatus())) {
                                    materialCardView.setBackgroundColor(Color.parseColor("#b00e29"));
                                    textView.setText("Falta computada!");
                                }
                            }
                        });
                    }
                } else {
                    savePresencaNaoComputada(presenca, materialCardView.getId(), textView.getId());
                }
            }

            @Override
            public void onFailure(Call<Presenca> call, Throwable t) {
                savePresencaNaoComputada(presenca, materialCardView.getId(), textView.getId());
            }
        });
    }

    private boolean savePresencaNaoComputada(Presenca presenca, Integer materialCardId, Integer textViewId){
        if (controller == null) {
            controller = new BancoController(context);
        }
        return controller.save(presenca.getData(), presenca.getIdAcademico(), presenca.getIdTurma(), presenca.getStatus(), materialCardId, textViewId);
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

    @Override
    public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
        if (beacons.size() == 0) {
            showToastMessage(getString(R.string.no_beacons_detected));
        }

        for (Beacon beacon : beacons) {
            if (beacon.getDistance() < 5){
                //Assim é possível implementar ações somente se o usuário estiver a menos de 5 metros de distância
            }

            showToastMessage(getString(R.string.beacon_detected, beacon.getId3()));
        }
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

        if (mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.disable();
        }
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
//        StringBuilder builder = new StringBuilder();

        RangeNotifier rangeNotifier = new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                if (beacons.size() > 0) {
//                    builder.delete(0, builder.length());
//                    builder.append("Quantidade de beacons localizado: ").append(beacons.size()).append("\n");

                    if (!beaconDistanceMap.isEmpty()){
                        beaconDistanceMap.clear();
                    }

                    for (Beacon beacon : beacons) {
//                        builder.append("ID1: "+beacon.getId1()).append("\n");
//                        builder.append("DISTANCIA em METROS: "+BigDecimal.valueOf(beacon.getDistance()).setScale(2, RoundingMode.HALF_UP)).append("\n");
//                        builder.append("DISTANCIA NOVO: "+BeaconUtils.calcularDistanciaByRssi(beacon)).append("\n");
//                        builder.append("====================").append("\n");

                        Double distance = BeaconUtils.calcularDistanciaByRssi(beacon);
                        beaconDistanceMap.put(beacon.getId1().toString(), distance);
                    }
                    //showToastMessage(builder.toString());
                }
            }

        };
        try {
            mBeaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
            mBeaconManager.addRangeNotifier(rangeNotifier);
            mBeaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
            mBeaconManager.addRangeNotifier(rangeNotifier);
        } catch (RemoteException e) {   }
    }

    private void getAulaDiaAcademico(){
        if (AppContext.getTurmaId() == null && AppContext.getNomeTurma() == null){
            API.getAulaDiaAcademico(new Callback<List<Turma>>() {
                @Override
                public void onResponse(Call<List<Turma>> call, Response<List<Turma>> response) {
                    List<Turma> turmas = response.body();
                    if (turmas != null && !turmas.isEmpty()) {
                        Turma turma = turmas.get(0);//A query vai retorna um resultado, foi tratado como list por conta de um erro relacionado a como o mysql retorna os valores
                        AppContext.setTurmaId(turma.getId().toString());
                        AppContext.setNomeTurma(turma.getDescricao());

                        TextView textViewNomeDisciplica = findViewById(R.id.nomeDisciplinaHoje);
                        textViewNomeDisciplica.setText(turma.getDescricao());
                    }
                }

                @Override
                public void onFailure(Call<List<Turma>> call, Throwable t) {
                }
            }, ID_SIMULATE_ACADEMICO, LocalDate.now().getDayOfWeek().toString());
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
        }, ID_SIMULATE_ACADEMICO, LocalDate.now().toString());
    }
}
