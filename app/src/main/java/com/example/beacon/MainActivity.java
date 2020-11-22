package com.example.beacon;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Collection;

public class MainActivity extends AppCompatActivity implements BeaconConsumer {
    protected static final String TAG = "MonitoringActivity";
    private BeaconManager beaconManager = null;
    private boolean entryMessageRaised = false;
    private boolean exitMessageRaised = false;
    private boolean rangingMessageRaised = false;

    private Region beaconRegion = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        initButtons();
        //onInit();
//        requestPermissions(new String[] {Manifest.permission.ACCESS_COARSE_LOCATION}, 1234);
//        beaconManager = BeaconManager.getInstanceForApplication(this);
//        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(BeaconParser.ALTBEACON_LAYOUT));
//        beaconManager.bind(this);

    }

    private void initButtons() {
        Button buttonRegistro = findViewById(R.id.buttonRegistro);
        buttonRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent itImc = new Intent(MainActivity.this, RegistroActivity.class);
            startActivity(itImc);
//                startBeaconMonitoring();
            }
        });
        Button buttonLogin = findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent itImc = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(itImc);
//                stopBeaconMonitoring();
            }
        });
    }

    private void showAlert(String title, String message){
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    @Override
    public void onBeaconServiceConnect() {
        Log.d(TAG, "Beacon Service Connect Called");
        beaconManager.setMonitorNotifier(new MonitorNotifier() {
            @Override
            public void didEnterRegion(Region region) {
                if(!entryMessageRaised){
                    showAlert("Did Enter in Region", region.getUniqueId() + region.getId1() + " - " + region.getId2() + " - " + region.getId3());
                    entryMessageRaised = true;
                }
            }

            @Override
            public void didExitRegion(Region region) {
                if(!exitMessageRaised){
                    showAlert("Did Exit in Region", region.getUniqueId() + region.getId1() + " - " + region.getId2() + " - " + region.getId3());
                    exitMessageRaised = true;
                }
            }

            @Override
            public void didDetermineStateForRegion(int i, Region region) {

            }
        });

        beaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                if (beacons.size() > 0) {
//                    Log.i(TAG, "The first beacon I see is about "+beacons.iterator().next().toString()+" meters away.");
                    if (!rangingMessageRaised) {
                        for (Beacon beacon : beacons) {
                            showAlert("Did Exit Region", region.getUniqueId() + " - Beacon Info: " + beacon.getId1() + " - " + beacon.getId2() + " - " + beacon.getId3());
                        }
                        rangingMessageRaised = true;
                    }
                }
            }
        });
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private void startBeaconMonitoring(){
        Log.d(TAG, "Start Beacon Monitoring Called");
        try {
            beaconRegion = new Region("MyBeacons", Identifier.parse("0AC59CA4-CFA6-442C-8C65-22247851344C"), Identifier.parse("4"), Identifier.parse("200"));
            beaconManager.startMonitoringBeaconsInRegion(beaconRegion);
            beaconManager.startRangingBeaconsInRegion(beaconRegion);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void stopBeaconMonitoring(){
        Log.d(TAG, "Stop Beacon Monitoring Called");
        try {
            beaconManager.stopMonitoringBeaconsInRegion(beaconRegion);
            beaconManager.stopRangingBeaconsInRegion(beaconRegion);
        } catch (RemoteException e) {
            Log.d(TAG, "KKKKKKKKKKKKKKKKKKKKK");
            e.printStackTrace();
        }
    }

}
