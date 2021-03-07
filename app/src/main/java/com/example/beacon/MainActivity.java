package com.example.beacon;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.beacon.api.API;
import com.example.beacon.api.models.Academico;
import com.example.beacon.context.AppContext;
import com.example.beacon.utils.Util;

import org.altbeacon.beacon.BeaconManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends Activity {
    private static final int PERMISSION_REQUEST_FINE_LOCATION = 1;
    private static final int PERMISSION_REQUEST_BACKGROUND_LOCATION = 2;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        verificarBluetooth();
        verificarDemaisPermission();

        //Variavel responsável pelo serviço de notificações
        //AppContext.setNotificationManager((NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE));

        EditText editText = findViewById(R.id.editEmail);
        editText.setText("95082");
        EditText editText2 = findViewById(R.id.editPassword);
        editText2.setText("95082");

        //TODO: setar no AppContext null em turmaId e academicoId

        initButtons();
    }

    private void initButtons(){
        Button buttonAuthLogin = findViewById(R.id.buttonAuthLogin);
        buttonAuthLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAuthAcademico();
            }
        });
    }

    private void onAuthAcademico() {
        EditText editEmail = findViewById(R.id.editEmail);//mas é código
        EditText editSenha = findViewById(R.id.editPassword);
        String codigo = editEmail.getText().toString();
        String senha = editSenha.getText().toString();
//        Intent itImc = new Intent(MainActivity.this, RequestPermissionActivity.class);
//        startActivity(itImc);
//        AppContext.setAcademicoId("1");
//        AppContext.setTurmaId("1");
        if (!Util.isNullOrEmpty(codigo) && !Util.isNullOrEmpty(senha)) {
            API.validarLogin(new Callback<List<Academico>>() {
                @Override
                public void onResponse(Call<List<Academico>> call, Response<List<Academico>> response) {
                    List<Academico> academicoList = response.body();
                    if (academicoList != null && !academicoList.isEmpty()){
                        System.out.println(academicoList.size());
                        Academico academico = academicoList.get(0);
                        AppContext.setAcademicoId(academico.getId());

                        Intent itImc = new Intent(MainActivity.this, RequestPermissionActivity.class);
                        startActivity(itImc);
                    } else {
                        Util.showToastMessage(context, "Não encontramos um usuário com essas credenciais.");
                    }
                }

                @Override
                public void onFailure(Call<List<Academico>> call, Throwable t) {
                    System.out.println("---------------------------------------------------------");
                    System.out.println(t.getMessage());
                    System.out.println("---------------------------------------------------------");
                    Util.showToastMessage(context, "Não foi possível realizar a autenticacao.");
                }
            }, codigo, senha);

        } else {
            Util.showToastMessage(context, "Não foi informado o E-mail ou a Senha!");
        }
    }

    private void verificarDemaisPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    if (this.checkSelfPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        if (!this.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                            builder.setTitle("This app needs background location access");
                            builder.setMessage("Please grant location access so this app can detect beacons in the background.");
                            builder.setPositiveButton(android.R.string.ok, null);
                            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                                @TargetApi(23)
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    requestPermissions(new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                                            PERMISSION_REQUEST_BACKGROUND_LOCATION);
                                }

                            });
                            builder.show();
                        } else {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                            builder.setTitle("Functionality limited");
                            builder.setMessage("Since background location access has not been granted, this app will not be able to discover beacons in the background.  Please go to Settings -> Applications -> Permissions and grant background location access to this app.");
                            builder.setPositiveButton(android.R.string.ok, null);
                            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {}
                            });
                            builder.show();
                        }
                    }
                }
            } else {
                if (!this.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION}, PERMISSION_REQUEST_FINE_LOCATION);
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Functionality limited");
                    builder.setMessage("Since location access has not been granted, this app will not be able to discover beacons.  Please go to Settings -> Applications -> Permissions and grant location access to this app.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                        }

                    });
                    builder.show();
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_FINE_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    Log.d(TAG, "fine location permission granted");
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Funcionalidade limitada");
                    builder.setMessage("Como o acesso à localização não foi concedido, este aplicativo não será capaz de descobrir beacons.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                        @Override
                        public void onDismiss(DialogInterface dialog) {
                        }

                    });
                    builder.show();
                }
                return;
            }
            case PERMISSION_REQUEST_BACKGROUND_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    Log.d(TAG, "background location permission granted");
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Functionality limited");
                    builder.setMessage("Since background location access has not been granted, this app will not be able to discover beacons when in the background.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                        @Override
                        public void onDismiss(DialogInterface dialog) {
                        }

                    });
                    builder.show();
                }
                return;
            }
        }
    }


    private void verificarBluetooth() {
        try {
            if (!BeaconManager.getInstanceForApplication(this).checkAvailability()) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Bluetooth not enabled");
                builder.setMessage("Please enable bluetooth in settings and restart this application.");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        //finish();
                        //System.exit(0);
                    }
                });
                builder.show();
            }
        }
        catch (RuntimeException e) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Bluetooth LE not available");
            builder.setMessage("Sorry, this device does not support Bluetooth LE.");
            builder.setPositiveButton(android.R.string.ok, null);
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                @Override
                public void onDismiss(DialogInterface dialog) {
                    //finish();
                    //System.exit(0);
                }
            });
            builder.show();
        }
    }
}
