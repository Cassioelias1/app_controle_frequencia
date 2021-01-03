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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestPermissionActivity extends AppCompatActivity implements BeaconConsumer, RangeNotifier {
    private static final String KEY_SIMULATE_BEACON = "5F469-D4GG-4AHA-SA5U0";
    private static final String ID_SIMULATE_ACADEMICO = "2";
    private Handler handler;

    protected final String TAG = RequestPermissionActivity.this.getClass().getSimpleName();
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    private static final int REQUEST_ENABLE_BLUETOOTH = 1;
    private static final long DEFAULT_SCAN_PERIOD_MS = 6000l;
    private static final String ALL_BEACONS_REGION = "AllBeaconsRegion";
    private static final String I_BEACON = "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24";

    private BeaconManager mBeaconManager;
    private Region mRegion;

    private Set<Integer> identificadoresBeacons = new HashSet<>();
    private BackgroundPowerSaver backgroundPowerSaver;
    private RegionBootstrap regionBootstrap;


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

//        MaterialCardView materialCardView1915 = findViewById(R.id.card);
//        TextView textView1915 = findViewById(R.id.textView1915);
//
//        MaterialCardView materialCardView2015 = findViewById(R.id.card2);
//        TextView textView2015 = findViewById(R.id.textView2015);
//
//        MaterialCardView materialCardView2100 = findViewById(R.id.card3);
//        TextView textView2100 = findViewById(R.id.textView2100);
//
//        MaterialCardView materialCardView2140 = findViewById(R.id.card4);
//        TextView textView2140 = findViewById(R.id.textView2140);

//        onInitThread(materialCardView1915, textView1915, 19, 15);
//        onInitThread(materialCardView2015, textView2015, 23, 11);
//        onInitThread(materialCardView2100, textView2100, 23, 11);
//        onInitThread(materialCardView2140, textView2140, 23, 11);

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

    private void onInitThread(final MaterialCardView materialCardView, final TextView textView, final Integer hour, final Integer minute) {
        final boolean[] presencaValidada = {false};
        new Thread() {
            @Override
            public void run() {
                while (!presencaValidada[0]) {
                    try {
                        LocalDateTime agora = LocalDateTime.now();
                        LocalDateTime primeiroHorario = LocalDateTime.of(agora.getYear(), agora.getMonth(), agora.getDayOfMonth(), hour, minute);
                        if (agora.getHour() == primeiroHorario.getHour() && agora.getMinute() == primeiroHorario.getMinute()){
                            presencaValidada[0] = true;

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
                                                    materialCardView.setBackgroundColor(Color.parseColor("#11A33F"));
                                                    textView.setText("Presença válidada!");
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
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
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

//    @Override
//    public void onBeaconServiceConnect() {
//        showToastMessage(getString(R.string.start_looking_for_beacons));
//
//        RangeNotifier rangeNotifier = new RangeNotifier() {
//            @Override
//            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
//                if (beacons.size() > 0) {
//                    //Log.d(TAG, "didRangeBeaconsInRegion called with beacon count:  "+beacons.size());
//                    Beacon firstBeacon = beacons.iterator().next();
//                    showToastMessage("The first beacon " + firstBeacon.toString() + " is about " + firstBeacon.getDistance() + " meters away.");
//                } else {
//                    showToastMessage("NAO ENCONTREI NADA");
//                }
//            }
//
//        };
//        try {
//            mBeaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
//            mBeaconManager.addRangeNotifier(rangeNotifier);
//            mBeaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
//            mBeaconManager.addRangeNotifier(rangeNotifier);
//        } catch (RemoteException e) {   }
//
////        try {
////            // Empezar a buscar los beacons que encajen con el el objeto Región pasado, incluyendo
////            // actualizaciones en la distancia estimada
////            mBeaconManager.startRangingBeaconsInRegion(mRegion);
////
////            showToastMessage(getString(R.string.start_looking_for_beacons));
////
////        } catch (RemoteException e) {
////            Log.d(TAG, "Se ha producido una excepción al empezar a buscar beacons " + e.getMessage());
////        }
////
////        mBeaconManager.addRangeNotifier(this);
//    }

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

            //calcula a distancia a partida do rssi
            double distanciaBeacon = BeaconUtils.calculateBeaconDistance(beacon);

            identificadoresBeacons.add(beacon.getId3().toInt());//Verificar qual é o id correto assim que tiver o beacon em mãos
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
        StringBuilder builder = new StringBuilder();

        RangeNotifier rangeNotifier = new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                if (beacons.size() > 0) {
                    builder.delete(0, builder.length());
                    builder.append("Quantidade de beacons localizado: ").append(beacons.size()).append("\n");
                    for (Beacon beacon : beacons) {
                        builder.append("ID: "+beacon.getId1()).append("\n");
                        builder.append("DISTANCIA: "+beacon.getDistance()).append("\n");
                        builder.append("=============").append("\n");
                    }
                    showToastMessage(builder.toString());
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
}
